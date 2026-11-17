package com.ecomm.reviewservice.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReviewRequest(

        @NotNull(message = "Məhsul seçilməlidir")
        UUID productId,

        @NotNull(message = "Reytinq daxil edilməlidir")
        @Min(value = 1, message = "Reytinq ən azı 1 olmalıdır")
        @Max(value = 5, message = "Reytinq ən çox 5 ola bilər")
        Integer rating,

        @Size(max = 1000, message = "Rəy ən çox 1000 simvol ola bilər")
        String comment
) {
}
