package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class ProductNotPurchasedException extends BaseException {

    public ProductNotPurchasedException() {
        super(ErrorCode.PRODUCT_NOT_PURCHASED);
    }
}