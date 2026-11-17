package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
