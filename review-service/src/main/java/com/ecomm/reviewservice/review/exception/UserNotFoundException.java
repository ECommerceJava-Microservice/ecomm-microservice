package com.ecomm.reviewservice.review.exception;

import com.ecomm.reviewservice.common.exception.BaseException;
import com.ecomm.reviewservice.common.exception.ErrorCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
