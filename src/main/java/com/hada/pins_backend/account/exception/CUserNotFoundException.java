package com.hada.pins_backend.account.exception;

/*
 * Created by parksuho on 2022/01/19.
 */
public class CUserNotFoundException extends RuntimeException {
    public CUserNotFoundException() { super(); }

    public CUserNotFoundException(String message) {
        super(message);
    }

    public CUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
