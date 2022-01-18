package com.hada.pins_backend.exception;

/*
 * Created by parksuho on 2022/01/19.
 */
public class CUnregisteredUserException extends RuntimeException {
    public CUnregisteredUserException() { super(); }

    public CUnregisteredUserException(String message) {
        super(message);
    }

    public CUnregisteredUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
