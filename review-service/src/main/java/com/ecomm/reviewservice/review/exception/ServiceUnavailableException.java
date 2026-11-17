package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException() {
        super(ErrorCode.SERVICE_UNAVAILABLE);
    }
}
