package com.hada.pins_backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hada.pins_backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/08.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("hadaPins");
    private static final long AUTH_TIME = 60 * 60 * 24 * 30 * 1000L; // 1달 유지 토큰

    /**
     * 토큰 생성
     */
    public static String createToken(User user, List<String> roles){
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("roles",roles)
                .sign(ALGORITHM);

    }
    /**
     * 헤더에서 토큰 가져오기
     */
    public static String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
    /**
     * 토큰 유효성 검사 및 유저 id 받아오기
     */
    public static VerifyResult verifyToken(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true)
                    .username(verify.getSubject()).build();
        }catch (Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false)
                    .username(decode.getSubject()).build();
        }
    }


}
