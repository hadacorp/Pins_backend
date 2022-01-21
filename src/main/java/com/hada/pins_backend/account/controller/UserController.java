package com.hada.pins_backend.account.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.service.UserService;
import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/19.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<ApiResponse> join(@RequestBody @Validated(ValidationSequence.class) JoinUserRequest request) {
        return ResponseEntity.ok().body(userService.join(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Validated(ValidationSequence.class) LoginUserRequest request) {
        return ResponseEntity.ok().body(userService.login(request));
    }

    // 로그인 한 유저 정보 확인
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> userInfo(@CurrentUser User user) {
        return ResponseEntity.ok().body(userService.userInfo(user.getId()));
    }

    // 가입 여부 확인
    @PostMapping("/old-user")
    public ResponseEntity<ApiResponse> oldUser(@RequestBody @Validated(ValidationSequence.class) LoginUserRequest request){
        return ResponseEntity.ok().body(userService.checkOldUser(request));
    }

    // 닉네임 중복 확인
    @PostMapping("/nickname")
    public ResponseEntity<ApiResponse> nickname(@RequestBody @Validated(ValidationSequence.class) CheckNickNameRequest request){
        return ResponseEntity.ok().body(userService.checkNickName(request));
    }
//
//    @GetMapping("/beforelogin")
//    public ResponseEntity<String> sms(@RequestParam String phoneNum) {
//        return userService.sms(phoneNum, "Y");
//    }
}
