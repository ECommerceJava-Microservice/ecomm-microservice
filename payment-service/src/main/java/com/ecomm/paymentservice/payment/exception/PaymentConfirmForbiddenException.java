package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class PaymentConfirmForbiddenException extends BaseException {

    public PaymentConfirmForbiddenException() {
        super(ErrorCode.PAYMENT_CONFIRM_FORBIDDEN);
    }
}
