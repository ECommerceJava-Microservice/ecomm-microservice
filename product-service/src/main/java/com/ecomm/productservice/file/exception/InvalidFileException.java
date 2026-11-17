package com.ecomm.productservice.file.exception;

import com.ecomm.productservice.common.exception.BaseException;
import com.ecomm.productservice.common.exception.ErrorCode;

public class InvalidFileException extends BaseException {

    public InvalidFileException() {
        super(ErrorCode.INVALID_FILE);
    }

    public InvalidFileException(String message) {
        super(ErrorCode.INVALID_FILE, message);
    }
}
