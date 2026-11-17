package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentNotRefundableException extends BaseException {

    public PaymentNotRefundableException() {
        super(ErrorCode.PAYMENT_NOT_REFUNDABLE);
    }
}