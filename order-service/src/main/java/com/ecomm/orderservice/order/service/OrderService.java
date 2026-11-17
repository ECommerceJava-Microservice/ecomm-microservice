package com.ecomm.orderservice.order.service;

import com.ecomm.orderservice.common.response.ApiResponse;
import com.ecomm.orderservice.common.response.PageResponse;
import com.ecomm.orderservice.order.dto.request.OrderFilterRequest;
import com.ecomm.orderservice.order.dto.request.OrderRequest;
import com.ecomm.orderservice.order.dto.response.OrderResponse;
import com.ecomm.orderservice.order.entity.OrderStatus;

import java.util.UUID;

public interface OrderService {

    ApiResponse<OrderResponse> placeOrder(OrderRequest request);

    ApiResponse<PageResponse<OrderResponse>> getMyOrders(OrderFilterRequest request);

    ApiResponse<OrderResponse> getOrderById(UUID orderId);

    ApiResponse<OrderResponse> cancelOrder(UUID orderId);

    ApiResponse<PageResponse<OrderResponse>> getAllOrders(OrderFilterRequest request);

    ApiResponse<OrderResponse> updateOrderStatus(UUID orderId, OrderStatus status);

    //  Review-service aid
    ApiResponse<Boolean> hasDeliveredProduct(UUID productId);
}
