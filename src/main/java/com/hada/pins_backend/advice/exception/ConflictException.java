package com.hada.pins_backend.advice.exception;

import com.hada.pins_backend.advice.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Created by parksuho on 2022/01/26.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends BusinessException{
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
