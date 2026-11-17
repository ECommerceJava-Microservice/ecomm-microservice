package com.ecomm.productservice.category.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class CategoryAlreadyExistsException extends BaseException {

    public CategoryAlreadyExistsException() {
        super(ErrorCode.CATEGORY_ALREADY_EXISTS);
    }

    public CategoryAlreadyExistsException(String message) {
        super(ErrorCode.CATEGORY_ALREADY_EXISTS, message);
    }
}
