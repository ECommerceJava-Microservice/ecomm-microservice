package com.ecomm.orderservice.order.messaging.async.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentRefundedEvent(
        UUID eventId,
        UUID orderId,
        Instant occurredAt
) {
}
