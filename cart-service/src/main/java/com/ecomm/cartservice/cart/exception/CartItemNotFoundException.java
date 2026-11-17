package com.ecomm.cartservice.cart.exception;

import com.ecomm.cartservice.common.exception.BaseException;
import com.ecomm.cartservice.common.exception.ErrorCode;

public class CartItemNotFoundException extends BaseException {

    public CartItemNotFoundException() {
        super(ErrorCode.CART_ITEM_NOT_FOUND);
    }
}
