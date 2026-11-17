package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class AddressNotFoundException extends BaseException {

    public AddressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }
}
