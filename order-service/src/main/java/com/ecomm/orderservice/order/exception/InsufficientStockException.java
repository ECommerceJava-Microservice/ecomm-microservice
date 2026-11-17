package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException() {
        super(ErrorCode.INSUFFICIENT_STOCK);
    }

    public InsufficientStockException(String message) {
        super(ErrorCode.INSUFFICIENT_STOCK, message);
    }
}
