package com.ecomm.orderservice.order.mapper;

import com.ecomm.orderservice.order.messaging.sync.cart.CartItemInfo;
import com.ecomm.orderservice.order.dto.request.OrderRequest;
import com.ecomm.orderservice.order.dto.response.OrderItemResponse;
import com.ecomm.orderservice.order.dto.response.OrderResponse;
import com.ecomm.orderservice.order.entity.Order;
import com.ecomm.orderservice.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "addressTitle", ignore = true)
    @Mapping(target = "addressCity", ignore = true)
    @Mapping(target = "addressStreet", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderRequest request);

    OrderItemResponse toItemResponse(OrderItem item);

    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem cartItemToOrderItem(CartItemInfo cartItem);
}
