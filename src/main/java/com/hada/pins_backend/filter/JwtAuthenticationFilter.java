package com.hada.pins_backend.filter;

import com.hada.pins_backend.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Start Auth Filter");
        // 헤더에서 JWT 를 받아옵니다.
        String token = JwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("Auth Filter: token {}",token);
        log.info("Auth Filter: vali {}",jwtTokenProvider.validateToken(token));

        HttpServletResponse res =(HttpServletResponse) response;

        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {

            try{// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                log.info("Auth Filter: after {}",jwtTokenProvider.getAuthentication(token).toString());
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }catch (UsernameNotFoundException e){
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }

    }
}
