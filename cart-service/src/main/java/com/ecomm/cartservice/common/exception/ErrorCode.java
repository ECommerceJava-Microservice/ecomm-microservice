package com.ecomm.cartservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Səbət elementi tapılmadı"),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Siz yalnız öz səbətinizi idarə edə bilərsiniz"),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "Stokda kifayət qədər məhsul yoxdur"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Məhsul tapılmadı"),
    PRODUCT_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Məhsul xidməti hazırda əlçatan deyil"),;

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
