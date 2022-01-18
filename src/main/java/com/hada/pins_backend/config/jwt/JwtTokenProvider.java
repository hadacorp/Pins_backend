package com.hada.pins_backend.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.TokenDto;
import com.hada.pins_backend.exception.CAuthenticationEntryPointException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

//    private static final Algorithm ALGORITHM = Algorithm.HMAC256("hadaPins");

    @Value("spring.jwt.secret")
    private String SECRET_KEY;

    private String ROLES = "roles";
    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1 hour
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; // 14 day
    private final UserDetailsService userDetailsService;

    @PostConstruct
    // Jwt 생성 시 서명으로 사용할 secret key를 BASE64로 인코딩
    protected void init() {
        SECRET_KEY = Base64UrlCodec.BASE64URL.encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 토큰 생성
     */
//    public static String createToken(User user, List<String> roles){
//        return JWT.create()
//                .withSubject(user.getPhoneNum())
//                .withExpiresAt(new Date(System.currentTimeMillis() + AUTH_TIME))
//                .withClaim("roles",roles)
//                .sign(ALGORITHM);
//
//    }
    public TokenDto createTokenDto(String phoneNumber, List<String> roles) {
        // Claims 에 user 구분을 위한 User pk 및 authorities 목록 삽입
        // Claims에 회원 구분할 수 있는 값 세팅하고 토큰 들어오면 해당 값으로 회원 구분해서 리소스 제공
        Claims claims = Jwts.claims().setSubject(phoneNumber);
        claims.put(ROLES, roles);

        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMillisecond)
                .build();
    }

    /**
     * JWT 토큰에서 인증 정보 조회
     */
//    public Authentication getAuthentication(String token) throws UsernameNotFoundException {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 회원 정보 추출
     */
//    public String getUserPk(String token) {
//        return JWT.require(ALGORITHM).build().verify(token).getSubject();
//    }

    // Jwt 토큰 복호화해서 가져오기
    // 만료된 토큰이여도 refresh token 검증 후 재발급 받을 수 있도록 claims 반환
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 헤더에서 토큰 가져오기
     */
    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        return null;
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }
}
