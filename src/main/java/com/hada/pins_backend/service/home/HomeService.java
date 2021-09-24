package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.home.LongitudeAndLatitude;
import com.hada.pins_backend.dto.home.response.HomeCardViewResponse;
import com.hada.pins_backend.dto.home.response.HomeLocationResponse;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
public interface HomeService {
    ResponseEntity<List<HomePinResponse>>loadPin(String phoneNum, LongitudeAndLatitude longitudeAndLatitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
    ResponseEntity<List<HomePinResponse>> searchPin(String phoneNum, String keyword, LongitudeAndLatitude longitudeAndLatitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
    ResponseEntity<List<HomeLocationResponse>> searchLocation(String keyword);
    ResponseEntity<List<HomeCardViewResponse>> loadCardView(String phoneNum, String pinType, Long pinDBId, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
    ResponseEntity<List<HomeCardViewResponse>> searchCardView(String phoneNum, String pinType, Long pinDBId, String keyword, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory);
}
