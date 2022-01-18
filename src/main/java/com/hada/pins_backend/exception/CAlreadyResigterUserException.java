package com.hada.pins_backend.exception;

/*
 * Created by parksuho on 2022/01/19.
 */
public class CAlreadyResigterUserException extends RuntimeException{
    public CAlreadyResigterUserException() { super(); }

    public CAlreadyResigterUserException(String message) {
        super(message);
    }

    public CAlreadyResigterUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
