package com.hada.pins_backend.exception.pin;

import com.hada.pins_backend.controller.PinController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * Created by bangjinhyuk on 2021/09/08.
 */
@RestControllerAdvice(basePackageClasses = PinController.class)
public class PinControllerAdvice {
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> ConstraintViolationException(ConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body 형식 오류");
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body 형식 오류");
    }
    @ExceptionHandler(value = NotExistException.class)
    public ResponseEntity<String> NotExistException(NotExistException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 핀 or 유저 id 입니다.");
    }

}
