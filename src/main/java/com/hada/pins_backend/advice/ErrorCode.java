package com.hada.pins_backend.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * Created by parksuho on 2022/01/27.
 * Modified by parksuho on 2022/03/25.
 * Modified by bangjinhyuk on 2022/03/27.
 * Modified by parksuho on 2022/04/01.
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
//    INVALID_REFRESH_TOKEN("리프레시 토큰이 유효하지 않습니다"),
//    MISMATCH_REFRESH_TOKEN("리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    INVALID_TOKEN("토큰이 비정상적입니다."),
    VALID_PROBLEM("Request Body 의 Valid 에 문제가 있습니다."),
    REQUEST_BODY_PROBLEM("Request Body 에 문제가 있습니다."),
    PROFILE_IMAGE_INVALID("프로필 사진에 문제가 있습니다."),
    ARGUMENT_INVALID("Path Variable 또는 Query Parameter 에 문제가 있습니다."),
    PATH_VARIABLE_MISSING("Path Variable 이 누락되었습니다."),
    PATH_VARIABLE_TYPE_ERROR("Path Variable 의 타입이 잘못되었습니다."),
    QUERY_PARAMETER_MISSING("Query Parameter 가 누락되었습니다."),
    QUERY_PARAMETER_BINDING_ERROR("Query Parameter 의 Valid 에 문제가 있습니다."),
    DATE_EXCEPTION("날짜에 문제가 있습니다."),
    COMMUNITY_IMAGE_INVALID("프로필 사진에 문제가 있습니다."),
    PIN_PARTICIPANTS_LIMIT_ERROR("참가인원을 초과했습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
//    INVALID_AUTH_TOKEN("권한 정보가 없는 토큰입니다"),
//    UNAUTHORIZED_MEMBER("현재 내 계정 정보가 존재하지 않습니다"),
    UNAUTHORIZED_USER("해당 리소스에 접근할 수 없는 권한입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    WRONG_URL("없는 URL 입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND("존재하지 않는 리프레쉬 토큰입니다."),
    USER_NOT_SUBSCRIBE("해당 유저는 해당 채팅방에 있지 않습니다."),

    /* 405 Method Not Allowed : 요청이 허용되지 않은 메소드 (GET,POST,PUT,DELETE,...) */
    WRONG_METHOD("잘못된 접근입니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    ALREADY_JOIN_USER("이미 가입한 회원입니다."),
    DUPLICATE_RESOURCE("데이터가 이미 존재합니다"),

    /* 500 : */
    PARSE_EXCEPTION("JSON 에러가 발생했습니다."),
    SERVER_ERROR("서버에 문제가 발생했습니다.")
    ;

    private final String message;
}
