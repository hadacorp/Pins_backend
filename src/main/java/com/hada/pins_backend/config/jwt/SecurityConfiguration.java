package com.hada.pins_backend.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/03/15.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //?
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 기본설정은 비 인증시 로그인 폼 화면으로 리다이렉트 되는데 미설정
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable() // 상태 저장하지 않으므로 csrf 보안 미설정

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미생성

                .and()
                .authorizeRequests()
                .antMatchers("/users/join", "/users/login", "/users/old-user", "/users/nickname").permitAll()
                .antMatchers("/exception/**").permitAll()
                .antMatchers("/h2-console/**", "/favicon.ico", "/chat/**", "/websocket/**").permitAll()
                .antMatchers("/users/me", "/users/update").hasRole("USER")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().denyAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                // jwt 인증 필터를 UsernamePasswordAuthenticationFilter.class 전에 넣는다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/docs/**");
        web.ignoring().antMatchers("/h2-console/**");
        // static 경로
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        // 더블 슬래시 허용
        web.httpFirewall(defaultHttpFirewall());
    }

    /*
    CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        //configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /*
    더블 슬래시 허용
     */
    @Bean
    public HttpFirewall defaultHttpFirewall() { //더블 슬래시 허용
        return new DefaultHttpFirewall();
    }
}