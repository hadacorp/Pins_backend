package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.dto.pin.response.MeetingPinResponse;
import com.hada.pins_backend.dto.pin.response.StoryPinResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
public interface PinService {
    ResponseEntity<String> createCommunityPin(User user, RequestCreateCommunityPin requestCreateCommunityPin);

    ResponseEntity<String> createMeetingPin(User user, RequestMeetingPin requestMeetingPin);

    ResponseEntity<String> createStoryPin(User user, RequestStoryPin requestStoryPin);

    ResponseEntity<MeetingPinResponse> getMeetingPin(Long id);

    ResponseEntity<StoryPinResponse> getStoryPin(Long id);
}
