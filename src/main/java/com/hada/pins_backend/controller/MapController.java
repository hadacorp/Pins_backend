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

}
