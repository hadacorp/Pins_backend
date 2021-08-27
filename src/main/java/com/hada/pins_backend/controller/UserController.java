package com.hada.pins_backend.controller;

import com.hada.pins_backend.config.JwtTokenProvider;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.NicknameDto;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import com.hada.pins_backend.service.CustomUserDetailService;
import com.hada.pins_backend.service.UserService;
import com.hada.pins_backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
//    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    // 회원가입
    @PostMapping("/users/join")
    public ResponseEntity<JoinUserResponse> join(@RequestBody @Valid JoinUserRequest userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insertUser(userDto));
    }

    //가입 여부 확인
    @PostMapping("/users/old-user")
    public Boolean oldUser(@RequestBody @Valid UserLoginForm userLoginForm){
        return userService.checkOldUser(userLoginForm);
    }

    //닉네임 중복 확인
    @PostMapping("/users/nickname")
    public Boolean nickname(@RequestBody @Valid NicknameDto nickname){
        return userService.checkNickname(nickname.getNickname());
    }

    // 로그인
    @PostMapping("/users/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid UserLoginForm userLoginForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(userLoginForm));
    }
//
//    //유저 정보 반환
//    @GetMapping("/users/getphonenum")
//    public String getPhonenum(){
//        log.info("Auth string : before");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        User user2 = (User) authentication2.getPrincipal();
//        log.info("Auth string : {}", user.getPhoneNum());
//        log.info("Auth string : {}", user2.getPhoneNum());
//        SecurityContextHolder.clearContext();
//        return user.getPhoneNum();
//
//    }

}
