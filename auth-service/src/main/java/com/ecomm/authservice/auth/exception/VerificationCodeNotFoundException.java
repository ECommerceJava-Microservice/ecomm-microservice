package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class VerificationCodeNotFoundException extends BaseException {

    public VerificationCodeNotFoundException() {
        super(ErrorCode.VERIFICATION_CODE_NOT_FOUND);
    }
}
