package com.ecomm.userservice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        LocalDateTime timestamp,
        Integer status,
        String message,
        T data,
        Object errors
) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                200,
                message,
                data,
                null
        );
    }

    public static <T> ApiResponse<T> error(
            int status,
            String message,
            Object errors
    ) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                null,
                errors
        );
    }
}
