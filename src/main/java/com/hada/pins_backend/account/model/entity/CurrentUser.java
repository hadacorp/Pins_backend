package com.hada.pins_backend.account.model.entity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/*
 * Created by parksuho on 2022/01/19.
 */
@Retention(RetentionPolicy.RUNTIME) //어느 시점까지 어노테이션의 메모리를 가져갈 지 설정, 런타임까지 유지
@Target(ElementType.PARAMETER) //어노테이션이 사용될 위치를 지정
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : user")
public @interface CurrentUser {
}