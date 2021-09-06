package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.service.pin.PinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@RestController
@RequestMapping("/pin")
public class PinController {
    private PinService pinService;


    @PostMapping("communitypin")
    public ResponseEntity<String> createCommunityPin(RequestCreateCommunityPin requestCreateCommunityPin){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getAuthorities();
        return pinService.createCommunityPin(user,requestCreateCommunityPin);
    }
}
