package com.hada.pins_backend.advice.exception;

/*
 * Created by parksuho on 2022/01/19.
 */
public class CAccessDeniedException extends RuntimeException {
    public CAccessDeniedException() { super(); }

    public CAccessDeniedException(String message) {
        super(message);
    }

    public CAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}