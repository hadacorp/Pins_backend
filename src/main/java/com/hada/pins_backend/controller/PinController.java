package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.service.pin.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pin")
public class PinController {
    private final PinService pinService;

    /**
     * 커뮤니티 핀 생성
     */
    @PostMapping("/communitypin")
    public ResponseEntity<String> createCommunityPin(@Valid RequestCreateCommunityPin requestCreateCommunityPin){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.createCommunityPin(user,requestCreateCommunityPin);
    }
    /**
     * 만남핀 생성
     */
    @PostMapping("/meetingpin")
    public ResponseEntity<String> createMeetingPin(@RequestBody @Valid RequestMeetingPin requestMeetingPin){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.createMeetingPin(user,requestMeetingPin);
    }
    /**
     * 이야기핀 생
     */
    @PostMapping("/storypin")
    public ResponseEntity<String> createStoryPin(@Valid RequestStoryPin requestStoryPin){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.createStoryPin(user,requestStoryPin);
    }


}
