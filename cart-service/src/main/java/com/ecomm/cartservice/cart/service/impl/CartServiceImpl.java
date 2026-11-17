package com.ecomm.cartservice.cart.service.impl;

import com.ecomm.cartservice.cart.messaging.sync.product.ProductInfo;
import com.ecomm.cartservice.cart.messaging.sync.product.ProductLookup;
import com.ecomm.cartservice.cart.dto.request.CartItemRequest;
import com.ecomm.cartservice.cart.dto.response.CartItemResponse;
import com.ecomm.cartservice.cart.dto.response.CartResponse;
import com.ecomm.cartservice.cart.entity.Cart;
import com.ecomm.cartservice.cart.entity.CartItem;
import com.ecomm.cartservice.cart.exception.CartAccessDeniedException;
import com.ecomm.cartservice.cart.exception.CartItemNotFoundException;
import com.ecomm.cartservice.cart.exception.InsufficientStockException;
import com.ecomm.cartservice.cart.repository.CartItemRepository;
import com.ecomm.cartservice.cart.repository.CartRepository;
import com.ecomm.cartservice.cart.service.CartService;
import com.ecomm.cartservice.common.response.ApiResponse;
import com.ecomm.cartservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductLookup productLookup;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public ApiResponse<CartResponse> getMyCart() {
        Cart cart = getOrCreateCart(SecurityUtils.getCurrentUserId());
        return ApiResponse.success(mapToResponse(cart), "Səbət məlumatları");
    }

    @Override
    @Transactional
    public ApiResponse<CartResponse> addItem(CartItemRequest cartItemRequest) {
        Cart cart = getOrCreateCart(SecurityUtils.getCurrentUserId());
        ProductInfo product = findProduct(cartItemRequest.productId());
        validateStock(product, cartItemRequest.quantity());

        cartItemRepository.findByCartAndProductId(cart, cartItemRequest.productId())
                .ifPresentOrElse(existing -> {
                            existing.setQuantity(existing.getQuantity() + cartItemRequest.quantity());
                            existing.calculateSubTotal();
                        },
                        () -> cart.addItem(CartItem.builder()
                                .cart(cart)
                                .productId(cartItemRequest.productId())
                                .quantity(cartItemRequest.quantity())
                                .price(product.price())
                                .build())
                );
        cart.updateTotalAmount();
        return ApiResponse.success(mapToResponse(cartRepository.save(cart)), "Məhsul səbətə əlavə edildi");
    }

    @Override
    @Transactional
    public ApiResponse<CartResponse> updateItem(UUID cartItemId, CartItemRequest cartItemRequest) {
        CartItem item = findCartItem(cartItemId);
        validateStock(findProduct(item.getProductId()), cartItemRequest.quantity());
        item.setQuantity(cartItemRequest.quantity());
        item.calculateSubTotal();
        item.getCart().updateTotalAmount();
        cartRepository.save(item.getCart());
        return ApiResponse.success(mapToResponse(item.getCart()), "Səbət yeniləndi");
    }

    @Override
    @Transactional
    public ApiResponse<CartResponse> removeItem(UUID cartItemId) {
        CartItem item = findCartItem(cartItemId);
        Cart cart = item.getCart();
        cart.removeItem(item);
        return ApiResponse.success(mapToResponse(cartRepository.save(cart)), "Məhsul səbətdən çıxarıldı");
    }

    @Override
    @Transactional
    public ApiResponse<Void> clearCart() {
        Cart cart = getOrCreateCart(SecurityUtils.getCurrentUserId());
        cart.getItems().clear();
        cart.updateTotalAmount();
        cartRepository.save(cart);
        return ApiResponse.success(null, "Səbət təmizləndi");
    }

//    Kafka üçün
    @Override
    @Transactional
    public void removeItemsByProductIds(UUID userId, List<UUID> productIds) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().removeIf(item -> productIds.contains(item.getProductId()));
            cart.updateTotalAmount();
            cartRepository.save(cart);
        });
    }

    //    ----Köməkçi metodlar -----------

    private Cart getOrCreateCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().userId(userId).build()
                ));
        for (CartItem item : cart.getItems()) {
            ProductInfo product = findProduct(item.getProductId());
            if (!item.getPrice().equals(product.price())) {
                item.setPrice(product.price());
            }
        }
        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }

    private CartItem findCartItem(UUID cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        if (!item.getCart().getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new CartAccessDeniedException();
        }
        return item;
    }

    private ProductInfo findProduct(UUID productId) {
        return productLookup.getProduct(productId);
    }

    private void validateStock(ProductInfo product, int quantity) {
        if (product.quantity() < quantity) {
            throw new InsufficientStockException();
        }
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> {
                    ProductInfo product = findProduct(item.getProductId());
                    return CartItemResponse.builder()
                            .cartItemId(item.getCartItemId())
                            .productId(item.getProductId())
                            .productName(product.productName())
                            .imageUrl(product.image())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .subTotal(item.getSubTotal())
                            .build();
                })
                .toList();
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .items(items)
                .itemCount(items.size())
                .totalAmount(cart.getTotalAmount())
                .build();
    }
}
