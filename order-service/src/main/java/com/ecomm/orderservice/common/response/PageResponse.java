package com.ecomm.orderservice.common.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        Boolean lastPage
) {
}
