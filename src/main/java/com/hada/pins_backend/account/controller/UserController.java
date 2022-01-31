package com.hada.pins_backend.account.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.service.UserService;
import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/19.
 * Modified by parksuho on 2022/01/26.
 * Modified by parksuho on 2022/01/27.
 * Modified by parksuho on 2022/01/30.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JoinUserResponse> join(
            @RequestPart(value = "profileImage") MultipartFile file,
            @RequestPart @Validated(ValidationSequence.class) JoinUserRequest request) throws IOException {
        return new ApiResponse<>(userService.join(file, request));
    }

    // 로그인
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TokenDto> login(@RequestBody @Validated(ValidationSequence.class) LoginUserRequest request) {
        return new ApiResponse<>(userService.login(request));
    }

    // 로그인 한 유저 정보 확인
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserDto> userInfo(@CurrentUser User user) {
        return new ApiResponse<>(userService.userInfo(user.getId()));
    }

    // 가입 여부 확인
    @PostMapping("/old-user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> oldUser(@RequestBody @Validated(ValidationSequence.class) LoginUserRequest request){
        return new ApiResponse<>(userService.checkOldUser(request));
    }

    // 닉네임 중복 확인
    @PostMapping("/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Boolean> nickname(@RequestBody @Validated(ValidationSequence.class) CheckNickNameRequest request){
        return new ApiResponse<>(userService.checkNickName(request));
    }
//
//    @GetMapping("/beforelogin")
//    public ResponseEntity<String> sms(@RequestParam String phoneNum) {
//        return userService.sms(phoneNum, "Y");
//    }
}
