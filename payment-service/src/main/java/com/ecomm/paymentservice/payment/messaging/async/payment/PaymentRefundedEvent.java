package com.ecomm.paymentservice.payment.messaging.async.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentRefundedEvent(
        UUID eventId,
        UUID orderId,
        Instant occurredAt
) {
}
