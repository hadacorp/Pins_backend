package com.hada.pins_backend.pin.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.InvalidException;

/*
 * Created by parksuho on 2022/04/05.
 */
public class CDateException extends InvalidException {
    public CDateException() { super(ErrorCode.DATE_EXCEPTION); }
}
