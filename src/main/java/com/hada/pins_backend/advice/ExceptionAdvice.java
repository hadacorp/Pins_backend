package com.hada.pins_backend.advice;

import com.hada.pins_backend.advice.exception.CAccessDeniedException;
import com.hada.pins_backend.advice.exception.CAlreadyResigterUserException;
import com.hada.pins_backend.advice.exception.CAuthenticationEntryPointException;
import com.hada.pins_backend.advice.exception.CUnregisteredUserException;
import com.hada.pins_backend.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/21.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    /***
     * Request Body 에서 Valid가 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> requestBodyNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("Request Body 의 Valid에 문제가 있습니다.",
                e.getBindingResult().getFieldErrors().stream().map(error ->
                        new ErrorResponse.Error(error.getField(),
                                error.getRejectedValue().toString(),
                                error.getDefaultMessage())).collect(Collectors.toList())));
    }

    /***
     * Request body 에 문제가 생겨 JSON 변환 실패
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> requestBodyWrongException(HttpServletRequest request, HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Request Body 에 문제가 있습니다."));
    }

    /**
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    protected ResponseEntity<ErrorResponse> authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("토큰이 비정상적입니다."));
    }

    /**
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> accessDeniedException(HttpServletRequest request, CAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("해당 리소스에 접근할 수 없는 권한입니다."));
    }

    /***
     * 핸드폰 번호가 등록되지 않은, 가입되지 않은 유저이면 발생하는 예외
     */
    @ExceptionHandler(CUnregisteredUserException.class)
    protected ResponseEntity<ErrorResponse> emailLoginFailedException(HttpServletRequest request, CUnregisteredUserException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("존재하지 않은 회원입니다."));
    }

    /***
     * 회원 가입 시 이미 로그인 된 이메일인 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAlreadyResigterUserException.class)
    protected ResponseEntity<ErrorResponse> emailSignupFailedException(HttpServletRequest request, CAlreadyResigterUserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("이미 가입한 회원입니다."));
    }

    /**
     * 틀린 URL 로 접근했을 경우 발생 시키는 예외
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> wrongURLException(HttpServletRequest request, NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("없는 URL 입니다."));
    }

    /**
     * 틀린 접근방식 (GET, POST, ..) 으로 접근했을 경우 발생 시키는 예외
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> wrongURLException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("틀린 접근입니다."));
    }
}