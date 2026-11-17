package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;
import com.ecomm.paymentservice.payment.entity.PaymentMethod;

public class PaymentMethodNotActiveException extends BaseException {

    public PaymentMethodNotActiveException(PaymentMethod method) {
        super(ErrorCode.PAYMENT_METHOD_NOT_ACTIVE, "Bu ödəniş üsulu sistemdə aktiv deyil: " + method);
    }
}
