package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class RefreshTokenExpiredException extends BaseException {

    public RefreshTokenExpiredException() {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
