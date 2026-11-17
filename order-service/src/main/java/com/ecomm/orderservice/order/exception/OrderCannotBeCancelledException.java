package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class OrderCannotBeCancelledException extends BaseException {

    public OrderCannotBeCancelledException() {
        super(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
    }
}
