package com.ecomm.productservice.product.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Məhsul adı boş ola bilməz.")
        @Size(min = 3, message = "Məhsul adı ən az {min} simvoldan ibarət olmalıdır.")
        String productName,

        @NotBlank(message = "Məhsul təsviri boş ola bilməz.")
        @Size(min = 10, message = "Məhsul təsviri ən az {min} simvol olmalıdır.")
        String description,

        @NotNull(message = "Məhsul sayı mütləq qeyd edilməlidir.")
        @Min(value = 1, message = "Məhsul sayı minimum {value} olmalıdır.")
        Integer quantity,

        @NotNull(message = "Məhsulun qiyməti boş ola bilməz.")
        @DecimalMin(value = "0.01", message = "Qiymət 0.01-dən aşağı ola bilməz.")
        BigDecimal price,

        @DecimalMin(value = "0.0", message = "Endirim mənfi ola bilməz.")
        @DecimalMax(value = "100.0", message = "Endirim 100%-i keçə bilməz.")
        BigDecimal discount,

        String image
) {

    public ProductRequest {
        discount = discount != null ? discount : BigDecimal.ZERO;
    }
}
