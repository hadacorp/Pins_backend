package com.hada.pins_backend.account.exception;

/*
 * Created by parksuho on 2022/01/19.
 */
public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException() { super(); }

    public CAuthenticationEntryPointException(String message) {
        super(message);
    }

    public CAuthenticationEntryPointException(String message, Throwable cause) {
        super(message, cause);
    }
}