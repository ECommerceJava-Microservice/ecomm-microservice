package com.ecomm.cartservice.cart.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CartItemResponse(
        UUID cartItemId,
        UUID productId,
        String productName,
        String imageUrl,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
