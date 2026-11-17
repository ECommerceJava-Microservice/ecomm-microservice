package com.ecomm.productservice.category.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }

    public CategoryNotFoundException(String message) {
        super(ErrorCode.CATEGORY_NOT_FOUND, message);
    }
}
