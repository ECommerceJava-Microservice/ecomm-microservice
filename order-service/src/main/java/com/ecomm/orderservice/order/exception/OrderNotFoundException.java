package com.ecomm.orderservice.order.exception;

import com.ecomm.orderservice.common.exception.BaseException;
import com.ecomm.orderservice.common.exception.ErrorCode;

public class OrderNotFoundException extends BaseException {

    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND);
    }
}
