package com.ecomm.userservice.address.exception;

import com.ecomm.userservice.common.exception.BaseException;
import com.ecomm.userservice.common.exception.ErrorCode;

public class AddressAccessDeniedException extends BaseException {

    public AddressAccessDeniedException() {
        super(ErrorCode.ADDRESS_ACCESS_DENIED);
    }
}