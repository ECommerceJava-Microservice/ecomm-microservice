package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException() {
        super(ErrorCode.SERVICE_UNAVAILABLE);
    }
}
