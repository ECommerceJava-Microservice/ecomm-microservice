package com.ecomm.cartservice.cart.exception;

import com.ecomm.cartservice.common.exception.BaseException;
import com.ecomm.cartservice.common.exception.ErrorCode;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException() {
        super(ErrorCode.INSUFFICIENT_STOCK);
    }
}
