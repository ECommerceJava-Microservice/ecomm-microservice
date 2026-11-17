package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException() {
        super(ErrorCode.SERVICE_UNAVAILABLE);
    }
}
