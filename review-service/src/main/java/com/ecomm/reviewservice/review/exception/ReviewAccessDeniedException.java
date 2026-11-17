package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class ReviewAccessDeniedException extends BaseException {

    public ReviewAccessDeniedException() {
        super(ErrorCode.REVIEW_ACCESS_DENIED);
    }
}