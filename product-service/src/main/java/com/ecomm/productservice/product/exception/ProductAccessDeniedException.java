package com.ecomm.productservice.product.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class ProductAccessDeniedException extends BaseException {

    public ProductAccessDeniedException() {
        super(ErrorCode.PRODUCT_ACCESS_DENIED);
    }

    public ProductAccessDeniedException(String message) {
        super(ErrorCode.PRODUCT_ACCESS_DENIED, message);
    }
}
