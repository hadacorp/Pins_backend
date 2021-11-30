package com.hada.pins_backend.controller;

import com.hada.pins_backend.dto.user.NicknameDto;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import com.hada.pins_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by bangjinhyuk on 2021/08/12.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
//    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<JoinUserResponse> join(@Valid JoinUserRequest userDto) {
        return userService.insertUser(userDto);
    }

    //가입 여부 확인
    @PostMapping("/old-user")
    public Boolean oldUser(@RequestBody @Valid UserLoginForm userLoginForm){
        return userService.checkOldUser(userLoginForm);
    }

    //닉네임 중복 확인
    @PostMapping("/nickname")
    public Boolean nickname(@RequestBody @Valid NicknameDto nickName){
        return userService.checkNickname(nickName.getNickName());
    }


    @GetMapping("/beforelogin")
    public ResponseEntity<String> sms(@RequestParam String phoneNum) {
        return userService.sms(phoneNum, "Y");
    }

    // 로그인
    @GetMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestParam @Valid UserLoginForm userLoginForm) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(userLoginForm));
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
