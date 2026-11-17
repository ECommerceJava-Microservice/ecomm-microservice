package com.ecomm.authservice.auth.exception;

import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.common.exception.ErrorCode;

public class RoleNotFoundException extends BaseException {

    public RoleNotFoundException() {
        super(ErrorCode.ROLE_NOT_FOUND);
    }
}
