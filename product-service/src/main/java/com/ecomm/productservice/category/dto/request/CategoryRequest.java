package com.ecomm.productservice.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequest(

        @NotBlank(message = "Kateqoriya adı boş ola bilməz")
        @Size(min = 3, max = 25, message = "Kateqoriya adı 3-25 simvol arasında olmalıdır")
        String categoryName,

        UUID parentId
) {
}
