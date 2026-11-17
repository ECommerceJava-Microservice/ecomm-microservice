package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentAlreadyCompletedException extends BaseException {

    public PaymentAlreadyCompletedException() {
        super(ErrorCode.PAYMENT_ALREADY_COMPLETED);
    }
}
