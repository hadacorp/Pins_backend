package com.hada.pins_backend.account.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.InvalidException;

/*
 * Created by parksuho on 2022/01/26.
 */
public class CProfileImageInvalidException extends InvalidException {
    public CProfileImageInvalidException() { super(ErrorCode.PROFILE_IMAGE_INVALID); }
}
