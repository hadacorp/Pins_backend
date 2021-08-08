package com.hada.pins_backend.filter;

import com.hada.pins_backend.config.JwtTokenProvider;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by bangjinhyuk on 2021/08/08.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 받아오기
        String token = JwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 토큰 유효성 검사
        if (token != null && JwtTokenProvider.verifyToken(token).isSuccess()){
           // TODO: 유효성 검사 후 유저 인증 정보 저장 및 처리 부분 완성하기
        }
    }
}
