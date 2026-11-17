package com.ecomm.userservice.address.exception;

import com.ecomm.userservice.common.exception.BaseException;
import com.ecomm.userservice.common.exception.ErrorCode;

public class AddressNotFoundException extends BaseException {

    public AddressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }

    public AddressNotFoundException(String message) {
        super(ErrorCode.ADDRESS_NOT_FOUND, message);
    }
}
