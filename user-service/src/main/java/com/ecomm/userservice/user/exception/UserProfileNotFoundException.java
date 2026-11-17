package com.ecomm.userservice.user.exception;

import com.ecomm.userservice.common.exception.BaseException;
import com.ecomm.userservice.common.exception.ErrorCode;

public class UserProfileNotFoundException extends BaseException {

    public UserProfileNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserProfileNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}
