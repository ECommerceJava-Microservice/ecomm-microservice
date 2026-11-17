package com.ecomm.orderservice.order.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID orderItemId,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
