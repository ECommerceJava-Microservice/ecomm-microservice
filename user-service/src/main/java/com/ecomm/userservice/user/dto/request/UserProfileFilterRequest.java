package com.ecomm.userservice.user.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UserProfileFilterRequest(

        String name,

        @Min(value = 0, message = "{page.number.min}")
        Integer pageNumber,

        @Min(value = 1, message = "{page.size.min}")
        @Max(value = 100, message = "{page.size.max}")
        Integer pageSize,

        String sortBy,

        String sortOrder

) {

    public UserProfileFilterRequest {
        if (pageNumber == null) {
            pageNumber = 0;
        }

        if (pageSize == null) {
            pageSize = 18;
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "userId";
        }

        if (sortOrder == null || sortOrder.isBlank()) {
            sortOrder = "asc";
        }
    }
}
