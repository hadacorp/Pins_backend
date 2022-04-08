package com.hada.pins_backend.pin.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.pin.model.request.CreateCommunityPinRequest;
import com.hada.pins_backend.pin.model.request.JoinPinRequest;
import com.hada.pins_backend.pin.model.request.CreateMeetingPinRequest;
import com.hada.pins_backend.pin.model.response.CommunityPinDto;
import com.hada.pins_backend.pin.model.response.MeetingPinDto;
import com.hada.pins_backend.pin.service.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/04/05.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pin")
public class PinController {
    private final PinService pinService;

    @PostMapping("/meeting")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMeetingPin(
            @CurrentUser User user,
            @RequestPart @Validated(ValidationSequence.class) CreateMeetingPinRequest request,
            @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        pinService.createMeetingPin(user.getId(), request, files);
    }

    @PostMapping("/community")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCommunityPin(
            @CurrentUser User user,
            @RequestPart @Validated(ValidationSequence.class) CreateCommunityPinRequest request,
            @RequestPart MultipartFile file) throws IOException {
        pinService.createCommunityPin(user.getId(), request, file);
    }

    @GetMapping("/meeting/{pinId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MeetingPinDto> meetingPinInfo(@PathVariable Long pinId) {
        return new ApiResponse<>(pinService.meetingPinInfo(pinId));
    }

    @GetMapping("/community/{pinId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CommunityPinDto> communityPinInfo(@PathVariable Long pinId) {
        return new ApiResponse<>(pinService.communityPinInfo(pinId));
    }

    @PostMapping("/meeting/{pinId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinMeetingPin(
            @CurrentUser User user, @PathVariable Long pinId,
            @RequestBody @Validated(ValidationSequence.class) JoinPinRequest request) {
        pinService.joinMeetingPin(user.getId(), pinId, request);
    }

    @PostMapping("/community/{pinId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinCommunityPin(
            @CurrentUser User user, @PathVariable Long pinId,
            @RequestBody @Validated(ValidationSequence.class) JoinPinRequest request) {
        pinService.joinCommunityPin(user.getId(), pinId, request);
    }
}
