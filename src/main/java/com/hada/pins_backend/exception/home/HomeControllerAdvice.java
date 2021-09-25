package com.hada.pins_backend.exception.home;

import com.hada.pins_backend.controller.HomeController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by bangjinhyuk on 2021/09/25.
 */
@RestControllerAdvice(basePackageClasses = HomeController.class)
public class HomeControllerAdvice {
    @ExceptionHandler(value = PintypeDBIdException.class)
    public ResponseEntity<String> PintypeDBIdException(PintypeDBIdException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pintype, PinDBId 오류");
    }
}
