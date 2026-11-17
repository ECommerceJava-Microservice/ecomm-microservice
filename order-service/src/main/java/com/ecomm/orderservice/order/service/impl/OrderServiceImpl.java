package com.ecomm.orderservice.order.service.impl;

import com.ecomm.orderservice.common.constants.AppConstants;
import com.ecomm.orderservice.common.response.ApiResponse;
import com.ecomm.orderservice.common.response.PageResponse;
import com.ecomm.orderservice.order.messaging.sync.cart.CartClient;
import com.ecomm.orderservice.order.messaging.sync.cart.CartInfo;
import com.ecomm.orderservice.order.messaging.sync.cart.CartItemInfo;
import com.ecomm.orderservice.order.messaging.sync.product.ProductClient;
import com.ecomm.orderservice.order.messaging.sync.product.StockChangeRequest;
import com.ecomm.orderservice.order.messaging.sync.user.AddressInfo;
import com.ecomm.orderservice.order.messaging.sync.user.UserClient;
import com.ecomm.orderservice.order.dto.request.OrderFilterRequest;
import com.ecomm.orderservice.order.dto.request.OrderRequest;
import com.ecomm.orderservice.order.dto.response.OrderResponse;
import com.ecomm.orderservice.order.entity.Order;
import com.ecomm.orderservice.order.entity.OrderItem;
import com.ecomm.orderservice.order.entity.OrderStatus;
import com.ecomm.orderservice.order.messaging.async.order.OrderEventPort;
import com.ecomm.orderservice.order.exception.AddressNotFoundException;
import com.ecomm.orderservice.order.exception.CartEmptyException;
import com.ecomm.orderservice.order.exception.InsufficientStockException;
import com.ecomm.orderservice.order.exception.OrderAccessDeniedException;
import com.ecomm.orderservice.order.exception.OrderCannotBeCancelledException;
import com.ecomm.orderservice.order.exception.OrderNotFoundException;
import com.ecomm.orderservice.order.mapper.OrderMapper;
import com.ecomm.orderservice.order.repository.OrderRepository;
import com.ecomm.orderservice.order.service.OrderService;
import com.ecomm.orderservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartClient cartClient;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderEventPort orderEventPort;

    // =====================================================================
    // ƏSAS METODLAR
    // =====================================================================

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<OrderResponse> placeOrder(OrderRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        // 1. Səbəti götür, boşdursa xəta
        CartInfo cart = cartClient.getMyCart().data();
        if (cart.items() == null || cart.items().isEmpty()) {
            throw new CartEmptyException();
        }

        // 2. Ünvanı yoxla
        AddressInfo address = findAddress(request.addressId());

        // 3 + 4. Stoku azalt, sifarişi yaz (xəta olsa stoku geri qaytar)
        List<CartItemInfo> reservedItems = new ArrayList<>();
        Order savedOrder;
        try {
            for (CartItemInfo item : cart.items()) {
                decreaseStock(item);
                reservedItems.add(item);
            }
            Order order = buildOrder(request, userId, address, cart.items());
            savedOrder = orderRepository.save(order);
        } catch (RuntimeException exception) {
            for (CartItemInfo item : reservedItems) {
                restoreStock(item.productId(), item.quantity());
            }
            throw exception;
        }

        // 5. Sifariş yerləşdirildi hadisəsini göndər (cart-service səbəti təmizləyir)
        try {
            orderEventPort.publishOrderPlaced(
                    savedOrder.getOrderId(),
                    userId,
                    cart.items().stream().map(CartItemInfo::productId).toList()
            );
        } catch (RuntimeException exception) {
            log.error("Sifariş yaradıldı, amma hadisə göndərilmədi: orderId={}",
                    savedOrder.getOrderId(), exception);
        }

        return ApiResponse.success(
                orderMapper.toResponse(savedOrder),
                "Sifariş uğurla yerləşdirildi"
        );
    }

    @Override
    public ApiResponse<PageResponse<OrderResponse>> getMyOrders(OrderFilterRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = buildPageable(request);

        Page<Order> page;
        if (request.getStatus() != null) {
            Specification<Order> spec = (root, query, cb) -> cb.and(
                    cb.equal(root.get("userId"), userId),
                    cb.equal(root.get("orderStatus"), request.getStatus())
            );
            page = orderRepository.findAll(spec, pageable);
        } else {
            page = orderRepository.findByUserId(userId, pageable);
        }

        return ApiResponse.success(buildPageResponse(page), "Sifarişləriniz");
    }

    @Override
    public ApiResponse<OrderResponse> getOrderById(UUID orderId) {
        Order order = findOrderByIdAndUser(orderId);
        return ApiResponse.success(orderMapper.toResponse(order), "Sifariş məlumatları");
    }

    @Override
    @Transactional
    public ApiResponse<OrderResponse> cancelOrder(UUID orderId) {
        Order order = findOrderByIdAndUser(orderId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderCannotBeCancelledException();
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        orderEventPort.publishOrderCancelled(savedOrder);

        return ApiResponse.success(orderMapper.toResponse(savedOrder), "Sifariş ləğv edildi");
    }

    @Override
    public ApiResponse<PageResponse<OrderResponse>> getAllOrders(OrderFilterRequest request) {
        Pageable pageable = buildPageable(request);

        Specification<Order> spec = (root, query, cb) ->
                request.getStatus() != null
                        ? cb.equal(root.get("orderStatus"), request.getStatus())
                        : cb.conjunction();

        Page<Order> page = orderRepository.findAll(spec, pageable);
        return ApiResponse.success(buildPageResponse(page), "Bütün sifarişlər");
    }

    @Override
    @Transactional
    public ApiResponse<OrderResponse> updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        OrderStatus previousStatus = order.getOrderStatus();
        order.setOrderStatus(status);
        Order savedOrder = orderRepository.save(order);

        if (status == OrderStatus.CANCELLED && previousStatus != OrderStatus.CANCELLED) {
            orderEventPort.publishOrderCancelled(savedOrder);
        }

        return ApiResponse.success(
                orderMapper.toResponse(savedOrder),
                "Sifarişin statusu yeniləndi"
        );
    }

//    Review-service aid
    @Override
    public ApiResponse<Boolean> hasDeliveredProduct(UUID productId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        boolean delivered = orderRepository.existsByUserIdAndOrderStatusAndItemsProductId(
                userId, OrderStatus.DELIVERED, productId);
        return ApiResponse.success(delivered, "Çatdırılma yoxlanıldı");
    }

    // =====================================================================
    // KÖMƏKÇİ METODLAR: monolitdən gələn
    // =====================================================================

    private Order buildOrder(
            OrderRequest request,
            UUID userId,
            AddressInfo address,
            List<CartItemInfo> cartItems
    ) {
        Order order = orderMapper.toEntity(request);
        order.setUserId(userId);
        order.setAddressTitle(address.title());
        order.setAddressCity(address.city());
        order.setAddressStreet(address.street());

        for (CartItemInfo cartItem : cartItems) {
            OrderItem orderItem = orderMapper.cartItemToOrderItem(cartItem);
            orderItem.setOrder(order);
            orderItem.calculateSubTotal();
            order.getItems().add(orderItem);
        }

        order.calculateTotalAmount();
        return order;
    }

    private Order findOrderByIdAndUser(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new OrderAccessDeniedException();
        }
        return order;
    }

    private Pageable buildPageable(OrderFilterRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase(AppConstants.SORT_ASC)
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
    }

    private PageResponse<OrderResponse> buildPageResponse(Page<Order> page) {
        List<OrderResponse> content = page.getContent().stream()
                .map(orderMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // =====================================================================
    // KÖMƏKÇİ METODLAR: 🔹 mikroservisə xas
    // =====================================================================

    private AddressInfo findAddress(UUID addressId) {
        return userClient.getMyAddresses().data().stream()
                .filter(address -> address.addressId().equals(addressId))
                .findFirst()
                .orElseThrow(AddressNotFoundException::new);
    }

    private void decreaseStock(CartItemInfo item) {
        try {
            productClient.decreaseStock(
                    new StockChangeRequest(item.productId(), item.quantity())
            );
        } catch (InsufficientStockException exception) {
            throw new InsufficientStockException(
                    "'" + item.productName() + "' məhsulu stokda kifayət qədər yoxdur"
            );
        }
    }

    private void restoreStock(UUID productId, Integer quantity) {
        try {
            productClient.increaseStock(new StockChangeRequest(productId, quantity));
        } catch (RuntimeException exception) {
            log.error("Stok bərpa edilmədi: productId={}, quantity={}",
                    productId, quantity, exception);
        }
    }

}