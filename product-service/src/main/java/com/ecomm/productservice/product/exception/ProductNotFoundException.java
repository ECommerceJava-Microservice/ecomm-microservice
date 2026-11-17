package com.ecomm.productservice.product.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(String message) {
        super(ErrorCode.PRODUCT_NOT_FOUND, message);
    }
}
