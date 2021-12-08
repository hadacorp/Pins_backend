package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.dto.pin.response.MeetingPinResponse;
import com.hada.pins_backend.dto.pin.response.StoryPinResponse;
import com.hada.pins_backend.service.pin.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.validation.Valid;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pin")
public class PinController {
    private final PinService pinService;

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

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
     * 이야기핀 생성
     */
    @PostMapping("/storypin")
    public ResponseEntity<String> createStoryPin(@Valid RequestStoryPin requestStoryPin){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.createStoryPin(user,requestStoryPin);
    }
    /**
     * 만남핀 가져오기
     */
    @GetMapping("/meetingpin/{id}")
    public ResponseEntity<MeetingPinResponse> getMeetingPin(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.getMeetingPin(id,user);
    }
    /**
     * 이야기핀 가져오기
     */
    @GetMapping("/storypin/{id}")
    public ResponseEntity<StoryPinResponse> getStoryPin(@PathVariable("id") Long id){
        return pinService.getStoryPin(id);
    }
    /**
     * 커뮤니티핀 삭제
     */
    @DeleteMapping ("/communitypin/{id}")
    public ResponseEntity<String> deleteCommunityPin(@PathVariable("id") Long id){
        return pinService.deleteCommunityPin(id);
    }
    /**
     * 만남핀 삭제
     */
    @DeleteMapping ("/meetingpin/{id}")
    public ResponseEntity<String> deleteMeetingPin(@PathVariable("id") Long id){
        return pinService.deleteMeetingPin(id);
    }
    /**
     * 이야기핀 삭제
     */
    @DeleteMapping ("/storypin/{id}")
    public ResponseEntity<String> deleteStoryPin(@PathVariable("id") Long id){
        return pinService.deleteStoryPin(id);
    }

    /**
     * 만남핀 참가 요청
     */
    @PostMapping("/meetingpin/{id}")
    public ResponseEntity<String> participantMeetingPin(@PathVariable("id") Long id, @RequestBody String greetings){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.participantMeetingPin(id,greetings,user);
    }

    /**
     * 내가 생성한 핀 목록 가져오기
     */
    @GetMapping("/meetingpin/manage/{id}")
    public ResponseEntity<String> getMyPinList(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return pinService.getMyPinList(id,user);
    }
}
