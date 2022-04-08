package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.pin.model.request.CreateCommunityPinRequest;
import com.hada.pins_backend.pin.model.request.JoinPinRequest;
import com.hada.pins_backend.pin.model.request.CreateMeetingPinRequest;
import com.hada.pins_backend.pin.model.response.CommunityPinDto;
import com.hada.pins_backend.pin.model.response.MeetingPinDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/04/05.
 */
public interface PinService {
    void createMeetingPin(Long userId, CreateMeetingPinRequest request, List<MultipartFile> files) throws IOException;
    void createCommunityPin(Long userId, CreateCommunityPinRequest request, MultipartFile file) throws IOException;
    MeetingPinDto meetingPinInfo(Long pinId);
    CommunityPinDto communityPinInfo(Long pinId);
    void joinMeetingPin(Long userId, Long pinId, JoinPinRequest request);
    void joinCommunityPin(Long userId, Long pinId, JoinPinRequest request);
}
