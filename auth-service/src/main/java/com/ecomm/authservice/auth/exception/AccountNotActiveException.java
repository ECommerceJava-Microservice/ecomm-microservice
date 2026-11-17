package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class AccountNotActiveException extends BaseException {

    public AccountNotActiveException() {
        super(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }
}
