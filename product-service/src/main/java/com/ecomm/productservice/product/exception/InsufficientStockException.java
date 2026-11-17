package com.ecomm.productservice.product.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException(String message) {
        super(ErrorCode.INSUFFICIENT_STOCK, message);
    }
}
