package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import org.springframework.http.ResponseEntity;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
public interface PinService {
    ResponseEntity<String> createCommunityPin(User user, RequestCreateCommunityPin requestCreateCommunityPin);
}
