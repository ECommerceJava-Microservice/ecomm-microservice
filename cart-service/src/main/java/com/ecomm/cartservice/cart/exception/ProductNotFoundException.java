package com.ecomm.cartservice.cart.exception;

import com.ecomm.cartservice.common.exception.BaseException;
import com.ecomm.cartservice.common.exception.ErrorCode;

public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
