package com.hada.pins_backend.advice;

import com.hada.pins_backend.advice.exception.BusinessException;
import com.hada.pins_backend.account.exception.CAccessDeniedException;
import com.hada.pins_backend.account.exception.CAuthenticationEntryPointException;
import com.hada.pins_backend.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/21.
 * Modified by parksuho on 2022/01/26.
 * Modified by parksuho on 2022/03/25.
 * Modified by bangjinhyuk on 2022/03/27.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * default Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse defaultException(HttpServletRequest request, Exception e) {
        log.error(String.valueOf(e));
        return new ErrorResponse(ErrorCode.SERVER_ERROR.getMessage());
    }

    /**
     * Request Body 에서 Valid 가 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse requestBodyNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            log.error("error field : \"{}\", value : \"{}\", message : \"{}\"", error.getField(), error.getRejectedValue(), error.getDefaultMessage());
        }
        return new ErrorResponse(ErrorCode.VALID_PROBLEM.getMessage());
    }

    /**
     * Request body 에 문제가 생겨 JSON 변환 실패
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse requestBodyWrongException(HttpServletRequest request, HttpMessageNotReadableException e) {
        return new ErrorResponse(ErrorCode.REQUEST_BODY_PROBLEM.getMessage());
    }

    /**
     * Parse Exception
     */
    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse parseException(HttpServletRequest request, ParseException e) {
        return new ErrorResponse(ErrorCode.PARSE_EXCEPTION.getMessage());
    }

    /*
     * 틀린 URL 로 접근했을 경우 발생 시키는 예외
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse wrongURLException(HttpServletRequest request, NoHandlerFoundException e) {
        return new ErrorResponse(ErrorCode.WRONG_URL.getMessage());
    }

    /*
     * Path Variable, Query Parameter 의 valid 가 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse argumentNotValidException(HttpServletRequest request, ConstraintViolationException e) {
        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            log.error("error field : \"{}\", value : \"{}\", message : \"{}\"",
                    error.getPropertyPath().toString().split(".")[1], error.getInvalidValue(), error.getMessage());
        }
        return new ErrorResponse(ErrorCode.ARGUMENT_INVALID.getMessage());
    }

    /*
     * Path Variable 가 누락된 예외
     */
    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse pathVariableMissingException(HttpServletRequest request, MissingPathVariableException e) {
        log.error("error field : \"{}\", message : \"{}\"", e.getVariableName(), e.getMessage());
        return new ErrorResponse(ErrorCode.PATH_VARIABLE_MISSING.getMessage());
    }

    /*
     * Path Variable 타입이 틀린 예외
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse pathVariableTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException  e) {
        return new ErrorResponse(ErrorCode.PATH_VARIABLE_TYPE_ERROR.getMessage());
    }

    /*
     * Query Parameter 가 누락된 예외
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse pathVariableMissingException(HttpServletRequest request, MissingServletRequestParameterException e) {
        log.error("error field : \"{}\", message : \"{}\"", e.getParameterName(), e.getMessage());
        return new ErrorResponse(ErrorCode.QUERY_PARAMETER_MISSING.getMessage());
    }

    /**
     * 틀린 접근방식 (GET, POST, ..) 으로 접근했을 경우 발생 시키는 예외
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected ErrorResponse wrongMethodException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse(ErrorCode.WRONG_METHOD.getMessage());
    }

    /**
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //
    protected ErrorResponse authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return new ErrorResponse(ErrorCode.INVALID_TOKEN.getMessage());
    }

    /**
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse accessDeniedException(HttpServletRequest request, CAccessDeniedException e) {
        return new ErrorResponse(ErrorCode.UNAUTHORIZED_USER.getMessage());
    }

    /**
     * 비즈니스 로직 예외
     */
    @ExceptionHandler(BusinessException.class)
    protected ErrorResponse businessException(HttpServletRequest request, BusinessException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Query Parameter Valid 문제
     */
    @ExceptionHandler(BindException.class)
    protected ErrorResponse BindException(HttpServletRequest request, BindException e) {
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            log.error("error field : \"{}\", value : \"{}\", message : \"{}\"", error.getField(), error.getRejectedValue(), error.getDefaultMessage());
        }
        return new ErrorResponse(ErrorCode.QUERY_PARAMETER_BINDING_ERROR.getMessage());
    }
}