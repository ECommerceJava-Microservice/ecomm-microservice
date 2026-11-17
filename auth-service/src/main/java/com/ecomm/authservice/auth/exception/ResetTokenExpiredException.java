package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class ResetTokenExpiredException extends BaseException {

    public ResetTokenExpiredException() {
        super(ErrorCode.RESET_TOKEN_EXPIRED);
    }
}
