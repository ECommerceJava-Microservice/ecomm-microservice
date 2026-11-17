package com.ecomm.reviewservice.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID reviewId,
        UUID productId,
        String productName,
        UUID userId,
        String userFullName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}