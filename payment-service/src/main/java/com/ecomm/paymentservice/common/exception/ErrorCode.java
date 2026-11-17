package com.ecomm.paymentservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Xidmət hazırda əlçatan deyil"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Sifariş tapılmadı"),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu sifariş üçün ödəniş etmək icazəniz yoxdur"),
    PAYMENT_CONFIRM_FORBIDDEN(HttpStatus.FORBIDDEN, "Nağd ödənişi yalnız admin təsdiq edə bilər"),
    PAYMENT_METHOD_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "Bu ödəniş üsulu sistemdə aktiv deyil"),
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Bu sifariş üçün ödəniş artıq mövcuddur"),
    ORDER_NOT_PAYABLE(HttpStatus.BAD_REQUEST, "Yalnız gözləmədə olan sifariş üçün ödəniş edilə bilər"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Ödəniş tapılmadı"),
    PAYMENT_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "Bu ödəniş artıq uğurla tamamlanıb"),
    PAYMENT_NOT_REFUNDABLE(HttpStatus.BAD_REQUEST, "Yalnız uğurlu ödənişlər geri qaytarıla bilər"),
    PAYMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Bu ödənişə baxmaq icazəniz yoxdur");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
