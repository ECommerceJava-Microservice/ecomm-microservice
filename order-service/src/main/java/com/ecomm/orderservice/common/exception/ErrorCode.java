package com.ecomm.orderservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Sifariş tapılmadı"),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu sifarişə müdaxilə etmək icazəniz yoxdur"),
    ORDER_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "Yalnız gözləmədə olan sifarişlər ləğv edilə bilər"),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "Səbətiniz boşdur"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Ünvan tapılmadı"),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "Stokda kifayət qədər məhsul yoxdur"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Xidmət hazırda əlçatan deyil");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
