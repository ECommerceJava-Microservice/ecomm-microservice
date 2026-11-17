package com.ecomm.orderservice.order.messaging.async.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID eventId,
        UUID orderId,
        Instant occurredAt
) {
}