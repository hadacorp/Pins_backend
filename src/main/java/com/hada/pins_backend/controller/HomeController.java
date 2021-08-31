package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.service.home.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/pin")
    public ResponseEntity<List<HomePinResponse>> pin(@RequestParam double latitude, @RequestParam double longitude){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return homeService.loadPin(user.getPhoneNum(),latitude,longitude);
    }
}
