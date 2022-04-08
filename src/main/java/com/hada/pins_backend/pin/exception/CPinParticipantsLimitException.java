package com.hada.pins_backend.pin.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.InvalidException;

/*
 * Created by parksuho on 2022/04/06.
 */
public class CPinParticipantsLimitException extends InvalidException {
    public CPinParticipantsLimitException() { super(ErrorCode.PIN_PARTICIPANTS_LIMIT_ERROR); }
}
