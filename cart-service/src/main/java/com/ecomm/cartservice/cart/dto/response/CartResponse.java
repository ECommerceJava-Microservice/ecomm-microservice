package com.ecomm.cartservice.cart.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CartResponse(
        UUID cartId,
        List<CartItemResponse> items,
        int itemCount,
        BigDecimal totalAmount
) {
}
