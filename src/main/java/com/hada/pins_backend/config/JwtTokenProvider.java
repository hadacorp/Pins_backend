package com.hada.pins_backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hada.pins_backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("hadaPins");
    private static final long AUTH_TIME = 60 * 60 * 24 * 30 * 1000L; // 1달 유지 토큰
    private final UserDetailsService userDetailsService;

    /**
     * 토큰 생성
     */
    public static String createToken(User user, List<String> roles){
        return JWT.create()
                .withSubject(user.getPhoneNum())
                .withExpiresAt(new Date(System.currentTimeMillis() + AUTH_TIME))
                .withClaim("roles",roles)
                .sign(ALGORITHM);

    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return JWT.require(ALGORITHM).build().verify(token).getSubject();
    }

    /**
     * 헤더에서 토큰 가져오기
     */
    public static String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     */
    public boolean validateToken(String jwtToken) {
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(jwtToken);
            log.info("1234");
            log.info("verify: {}",verify.getExpiresAt().before(new Date())+"");
            return !verify.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
