package com.ecomm.cartservice.cart.messaging.async.order;

import java.util.List;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID eventId,
        UUID userId,
        List<UUID> productIds
) {
}
