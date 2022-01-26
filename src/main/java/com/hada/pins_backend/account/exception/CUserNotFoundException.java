package com.hada.pins_backend.account.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.EntityNotFoundException;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/26.
 */
public class CUserNotFoundException extends EntityNotFoundException {
    public CUserNotFoundException() { super(ErrorCode.USER_NOT_FOUND); }
}
