package com.ecomm.paymentservice.payment.exception;

import com.ecomm.paymentservice.common.exception.BaseException;
import com.ecomm.paymentservice.common.exception.ErrorCode;

public class OrderNotPayableException extends BaseException {

    public OrderNotPayableException() {
        super(ErrorCode.ORDER_NOT_PAYABLE);
    }
}