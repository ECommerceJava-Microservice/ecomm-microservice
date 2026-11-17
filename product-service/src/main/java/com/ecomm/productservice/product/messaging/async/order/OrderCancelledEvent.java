package com.ecomm.productservice.product.messaging.async.order;

import java.util.List;
import java.util.UUID;

public record OrderCancelledEvent(
        UUID eventId,
        UUID orderId,
        List<Item> items
) {

    public record Item(UUID productId, Integer quantity) {
    }
}
