package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class ReviewNotFoundException extends BaseException {

    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND);
    }
}
