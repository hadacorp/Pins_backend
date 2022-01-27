package com.hada.pins_backend.account.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.ConflictException;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/27.
 */
public class CAlreadyJoinUserException extends ConflictException {
    public CAlreadyJoinUserException() { super(ErrorCode.ALREADY_JOIN_USER); }
}
