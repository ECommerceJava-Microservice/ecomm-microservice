package com.ecomm.orderservice.order.messaging.sync.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CartItemInfo(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
