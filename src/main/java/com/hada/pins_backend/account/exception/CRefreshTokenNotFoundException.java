package com.hada.pins_backend.account.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.EntityNotFoundException;

public class CRefreshTokenNotFoundException extends EntityNotFoundException {
    public CRefreshTokenNotFoundException() { super(ErrorCode.REFRESH_TOKEN_NOT_FOUND); }
}
