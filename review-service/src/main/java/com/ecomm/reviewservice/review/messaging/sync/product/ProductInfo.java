package com.ecomm.reviewservice.review.messaging.sync.product;

import java.util.UUID;

public record ProductInfo(
        UUID productId,
        String productName
) {
}