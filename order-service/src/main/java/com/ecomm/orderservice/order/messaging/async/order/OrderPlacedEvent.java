package com.ecomm.orderservice.order.messaging.async.order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID eventId,
        UUID orderId,
        UUID userId,
        List<UUID> productIds,
        Instant occurredAt
) {
}
