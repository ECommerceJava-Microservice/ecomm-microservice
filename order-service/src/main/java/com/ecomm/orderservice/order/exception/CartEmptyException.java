package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class CartEmptyException extends BaseException {

    public CartEmptyException() {
        super(ErrorCode.CART_EMPTY);
    }
}
