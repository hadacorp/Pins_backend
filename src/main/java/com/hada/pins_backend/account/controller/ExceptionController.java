package com.hada.pins_backend.account.controller;

import com.hada.pins_backend.exception.CAccessDeniedException;
import com.hada.pins_backend.exception.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Created by parksuho on 2022/01/19.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {
    @GetMapping("/entryPoint")
    public ResponseEntity entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping("/accessDenied")
    public ResponseEntity accessDeniedException() {
        throw new CAccessDeniedException();
    }
}
