package com.ecomm.productservice.category.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CategoryResponse(
        UUID categoryId,
        String categoryName,
        UUID parentId,
        List<CategoryResponse> children
) {

    public CategoryResponse {
        children = children != null ? children : new ArrayList<>();
    }
}
