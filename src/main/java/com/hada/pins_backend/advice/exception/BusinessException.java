package com.hada.pins_backend.advice.exception;

import com.hada.pins_backend.advice.ErrorCode;

/*
 * Created by parksuho on 2022/01/26.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
