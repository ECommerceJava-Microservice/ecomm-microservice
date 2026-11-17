package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class TooManyAttemptsException extends BaseException {

    public TooManyAttemptsException() {
        super(ErrorCode.TOO_MANY_ATTEMPTS);
    }
}
