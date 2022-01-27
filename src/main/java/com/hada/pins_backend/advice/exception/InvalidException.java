package com.hada.pins_backend.advice.exception;

import com.hada.pins_backend.advice.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by parksuho on 2022/01/27.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidException extends BusinessException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
