package com.ecomm.paymentservice.payment.dto.response;

import com.ecomm.paymentservice.payment.entity.PaymentMethod;
import com.ecomm.paymentservice.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        UUID orderId,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
