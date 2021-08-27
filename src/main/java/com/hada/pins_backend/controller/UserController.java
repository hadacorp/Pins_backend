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
import com.hada.pins_backend.service.CustomUserDetailService;
import com.hada.pins_backend.service.UserService;
import com.hada.pins_backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public JoinUserResponse join(@RequestBody @Valid JoinUserRequest userDto) {
        return userService.insertUser(userDto);
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

//    // 로그인
//    @PostMapping("/users/login")
//    public String login(@RequestBody Map<String, String> user) {
//        User member = userRepository.findByPhoneNum(user.get("phonenum"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
//        log.info("User Roles : {}", member.getRoles());
//        return jwtTokenProvider.createToken(member, member.getRoles());
//    }
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
