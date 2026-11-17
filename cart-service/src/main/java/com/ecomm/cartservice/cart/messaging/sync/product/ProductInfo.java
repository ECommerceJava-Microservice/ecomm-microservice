package com.ecomm.cartservice.cart.messaging.sync.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductInfo(
        UUID productId,
        String productName,
        String image,
        Integer quantity,
        BigDecimal price
) {
}
