package com.ecomm.cartservice.cart.exception;

import com.ecomm.cartservice.common.exception.BaseException;
import com.ecomm.cartservice.common.exception.ErrorCode;

public class CartAccessDeniedException extends BaseException {
    public CartAccessDeniedException() {
        super(ErrorCode.CART_ACCESS_DENIED);
    }
}
