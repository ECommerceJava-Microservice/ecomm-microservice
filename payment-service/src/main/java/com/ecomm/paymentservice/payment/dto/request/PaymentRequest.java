package com.ecomm.paymentservice.payment.dto.request;

import com.ecomm.paymentservice.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentRequest(
        @NotNull(message = "Sifariş seçilməlidir")
        UUID orderId,

        @NotNull(message = "Ödəniş üsulu seçilməlidir")
        PaymentMethod paymentMethod
) {
}
