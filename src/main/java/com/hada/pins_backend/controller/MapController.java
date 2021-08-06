package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bangjinhyuk on 2021/08/06.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/map")
public class MapController {

    @GetMapping("/getMeetingPin")
    public MeetingPin pins(){
        return new MeetingPin();
    }
}
