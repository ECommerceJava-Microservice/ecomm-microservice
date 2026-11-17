package com.ecomm.productservice.product.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID productId,
        String productName,
        String image,
        String description,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        BigDecimal specialPrice,
        UUID categoryId,
        String categoryName
) {
}
