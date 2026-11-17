package com.ecomm.orderservice.order.messaging.async.order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCancelledEvent(
        UUID eventId,
        UUID orderId,
        UUID userId,
        List<Item> items,
        Instant occurredAt
) {

    public record Item(UUID productId, Integer quantity) {
    }
}
