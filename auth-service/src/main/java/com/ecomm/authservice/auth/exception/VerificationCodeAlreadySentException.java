package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class VerificationCodeAlreadySentException extends BaseException {

    public VerificationCodeAlreadySentException() {
        super(ErrorCode.VERIFICATION_CODE_ALREADY_SENT);
    }
}
