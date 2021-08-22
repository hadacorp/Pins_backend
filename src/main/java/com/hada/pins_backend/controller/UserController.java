package com.hada.pins_backend.controller;

import com.hada.pins_backend.config.JwtTokenProvider;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .phoneNum(user.get("phonenum"))
                .name("bang")
                .nickName("bbangi")
                .resRedNumber("9801031")
                .age(24)
                .gender(Gender.Male)
                .image("http;//...")
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    }
    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByPhoneNum(user.get("phonenum"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        log.info("User Roles : {}", member.getRoles());
        return jwtTokenProvider.createToken(member, member.getRoles());
    }


}
