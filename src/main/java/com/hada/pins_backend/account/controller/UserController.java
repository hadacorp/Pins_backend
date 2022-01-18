package com.hada.pins_backend.account.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.service.UserService;
import com.hada.pins_backend.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<JoinUserResponse>> join(@RequestBody @Valid JoinUserRequest request) {
        return ResponseEntity.ok().body(userService.join(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody @Valid LoginUserRequest request) {
//        return userService.login(request);
        return ResponseEntity.ok().body(userService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> userInfo(@CurrentUser User user) {
        return ResponseEntity.ok().body(userService.userInfo(user.getId()));
    }
//
//    //가입 여부 확인
//    @PostMapping("/old-user")
//    public Boolean oldUser(@RequestBody @Valid UserLoginForm userLoginForm){
//        return userService.checkOldUser(userLoginForm);
//    }
//
//    //닉네임 중복 확인
//    @PostMapping("/nickname")
//    public Boolean nickname(@RequestBody @Valid NicknameDto nickName){
//        return userService.checkNickname(nickName.getNickName());
//    }
//
//
//    @GetMapping("/beforelogin")
//    public ResponseEntity<String> sms(@RequestParam String phoneNum) {
//        return userService.sms(phoneNum, "Y");
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
