package com.hada.pins_backend.exception.user;

import com.hada.pins_backend.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 아이디 입니다.");
    }
}
