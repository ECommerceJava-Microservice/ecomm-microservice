package com.ecomm.orderservice.order.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequest(

        @NotNull(message = "Ünvan seçilməlidir")
        UUID addressId,

        String note
) {
}
