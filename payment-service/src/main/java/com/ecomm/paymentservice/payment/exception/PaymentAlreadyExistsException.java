package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentAlreadyExistsException extends BaseException {

    public PaymentAlreadyExistsException() {
        super(ErrorCode.PAYMENT_ALREADY_EXISTS);
    }
}
