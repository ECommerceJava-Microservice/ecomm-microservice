package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class AccountAlreadyActiveException extends BaseException {

    public AccountAlreadyActiveException() {
        super(ErrorCode.ACCOUNT_ALREADY_ACTIVE);
    }
}
