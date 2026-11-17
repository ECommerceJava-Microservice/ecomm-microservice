package com.ecomm.paymentservice.payment.messaging.async.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID eventId,
        UUID orderId,
        Instant occurredAt
) {
}
