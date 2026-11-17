package com.ecomm.cartservice.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CartItemRequest(

        @NotNull(message = "Məhsul id-si boş ola bilməz")
        UUID productId,

        @NotNull(message = "Miqdar boş ola bilməz")
        @Min(value = 1, message = "Miqdar ən azı 1 olmalıdır")
        Integer quantity
) {
}
