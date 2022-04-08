package com.hada.pins_backend.pin.exception;

import com.hada.pins_backend.advice.ErrorCode;
import com.hada.pins_backend.advice.exception.InvalidException;

/*
 * Created by parksuho on 2022/04/05.
 */
public class CCommunityImageException  extends InvalidException {
    public CCommunityImageException() { super(ErrorCode.COMMUNITY_IMAGE_INVALID); }
}

