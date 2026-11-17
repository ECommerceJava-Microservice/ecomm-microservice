package com.ecomm.userservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User tapılmadı"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND,"Ünvan tapılmadı"),
    ADDRESS_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu ünvana müdaxilə etmək icazəniz yoxdur.");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
