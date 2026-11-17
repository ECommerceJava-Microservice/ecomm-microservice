package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentAccessDeniedException extends BaseException {

    public PaymentAccessDeniedException() {
        super(ErrorCode.PAYMENT_ACCESS_DENIED);
    }
}
