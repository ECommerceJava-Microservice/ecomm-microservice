package com.ecomm.orderservice.order.messaging.sync.product;

import java.util.UUID;

public record StockChangeRequest(
        UUID productId,
        Integer quantity
) {
}
