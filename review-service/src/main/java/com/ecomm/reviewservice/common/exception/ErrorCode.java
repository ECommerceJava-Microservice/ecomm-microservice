package com.ecomm.reviewservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Xidmət hazırda əlçatan deyil"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Sifariş tapılmadı"),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu sifariş üçün ödəniş etmək icazəniz yoxdur"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Məhsul tapılmadı"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "İstifadəçi tapılmadı"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "Rəy tapılmadı"),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "Bu məhsula artıq rəy yazmısınız"),
    PRODUCT_NOT_PURCHASED(HttpStatus.BAD_REQUEST, "Yalnız aldığınız və çatdırılmış məhsula rəy yaza bilərsiniz"),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu rəyə müdaxilə etmək icazəniz yoxdur"),;

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
