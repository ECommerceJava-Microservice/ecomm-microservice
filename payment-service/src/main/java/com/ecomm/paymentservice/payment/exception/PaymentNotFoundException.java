package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentNotFoundException extends BaseException {

    public PaymentNotFoundException() {
        super(ErrorCode.PAYMENT_NOT_FOUND);
    }
}
