package com.hada.pins_backend.chatting.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.EntityNotFoundException;

/*
 * Created by parksuho on 2022/03/26.
 */
public class CUserNotSubscribeException extends EntityNotFoundException {
    public CUserNotSubscribeException() { super(ErrorCode.USER_NOT_SUBSCRIBE); }
}
