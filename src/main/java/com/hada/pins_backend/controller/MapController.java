package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by bangjinhyuk on 2021/08/06.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MapController {

    @GetMapping("/getMeetingPin")
    public MeetingPin pins(){
        return new MeetingPin();
    }

    //유저 정보 반환
    @GetMapping("/getGender")
    public String getGender(){
        log.info("Auth string : before");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("Auth string : {}", user.getPhoneNum());
        SecurityContextHolder.clearContext();
        return user.getPhoneNum();
    }
}
