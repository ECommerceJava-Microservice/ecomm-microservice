package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class VerificationCodeExpiredException extends BaseException {

    public VerificationCodeExpiredException() {
        super(ErrorCode.VERIFICATION_CODE_EXPIRED);
    }
}
