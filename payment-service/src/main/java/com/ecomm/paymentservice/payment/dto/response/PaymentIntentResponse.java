package com.ecomm.paymentservice.payment.dto.response;

import java.util.UUID;

public record PaymentIntentResponse(
        String clientSecret,
        String paymentIntentId,
        UUID orderId
) {
}
