package com.hada.pins_backend.account.controller;

import com.hada.pins_backend.advice.exception.CAccessDeniedException;
import com.hada.pins_backend.advice.exception.CAuthenticationEntryPointException;
import com.hada.pins_backend.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/26.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {
    @GetMapping("/entryPoint")
    public ErrorResponse entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping("/accessDenied")
    public ErrorResponse accessDeniedException() {
        throw new CAccessDeniedException();
    }
}
