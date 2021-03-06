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
 * Modified by parksuho on 2022/03/25.
 * Modified by parksuho on 2022/03/26.
 * Modified by parksuho on 2022/03/27.
 * Modified by parksuho on 2022/04/06.
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
                .httpBasic().disable() // ??????????????? ??? ????????? ????????? ??? ???????????? ??????????????? ????????? ?????????
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable() // ?????? ???????????? ???????????? csrf ?????? ?????????

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ?????? ?????????

                .and()
                .authorizeRequests()
                .antMatchers("/users/join", "/users/login", "/users/old-user", "/users/nickname").permitAll()
                .antMatchers("/exception/**", "/test/**").permitAll()
                .antMatchers("/h2-console/**", "/chatting/**").permitAll()
                .antMatchers("/users/me", "/users/update", "/v1/home/**", "/chat/**", "/pub/**", "/sub/**").hasRole("USER")
                .antMatchers("/pin/**").hasRole("USER")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().denyAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                // jwt ?????? ????????? UsernamePasswordAuthenticationFilter.class ?????? ?????????.
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/docs/**");
        web.ignoring().antMatchers("/h2-console/**");
        // static ??????
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        // ?????? ????????? ??????
        web.httpFirewall(defaultHttpFirewall());
    }

    /*
    CORS ??????
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://dev.pins.live:8081");
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
    ?????? ????????? ??????
     */
    @Bean
    public HttpFirewall defaultHttpFirewall() { //?????? ????????? ??????
        return new DefaultHttpFirewall();
    }
}