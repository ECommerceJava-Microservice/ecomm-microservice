package com.ecomm.orderservice.order.dto.response;

import com.ecomm.orderservice.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String addressTitle,
        String addressCity,
        String addressStreet,
        OrderStatus orderStatus,
        BigDecimal totalAmount,
        String note,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
