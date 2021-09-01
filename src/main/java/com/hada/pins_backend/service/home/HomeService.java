package com.hada.pins_backend.service.home;

import com.hada.pins_backend.dto.home.response.HomePinResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
public interface HomeService {
    ResponseEntity<List<HomePinResponse>>loadPin(String phoneNum, double latitude, double logitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
    ResponseEntity<List<HomePinResponse>> searchPin(String phoneNum, String keyword, double latitude, double longitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
}
