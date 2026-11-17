package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class ResetTokenInvalidException extends BaseException {

    public ResetTokenInvalidException() {
        super(ErrorCode.RESET_TOKEN_INVALID);
    }
}
