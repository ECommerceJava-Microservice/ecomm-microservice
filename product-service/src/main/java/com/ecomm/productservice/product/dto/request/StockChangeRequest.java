package com.ecomm.productservice.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

//Order-service üçün
public record StockChangeRequest(
        @NotNull(message = "Məhsul seçilməlidir")
        UUID productId,

        @NotNull(message = "Miqdar daxil edilməlidir")
        @Min(value = 1, message = "Miqdar ən azı 1 olmalıdır")
        Integer quantity
) {
}
