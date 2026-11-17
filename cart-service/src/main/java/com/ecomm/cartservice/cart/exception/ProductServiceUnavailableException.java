package com.ecomm.cartservice.cart.exception;

import com.ecomm.cartservice.common.exception.BaseException;
import com.ecomm.cartservice.common.exception.ErrorCode;

public class ProductServiceUnavailableException extends BaseException {

    public ProductServiceUnavailableException() {
        super(ErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
    }
}
