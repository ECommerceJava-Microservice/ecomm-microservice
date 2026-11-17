package com.ecomm.productservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Kateqoriya tapılmadı"),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "Bu adda kateqoriya artıq mövcuddur!"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Məhsul tapılmadı"),
    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Siz yalnız öz məhsullarınızı idarə edə bilərsiniz"),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "Yüklənən fayl düzgün deyil"),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "Stokda kifayət qədər məhsul yoxdur");;

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
