package com.hada.pins_backend.pin.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.advice.exception.BusinessException;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.*;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinRepositorySupport;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRepositorySupport;
import com.hada.pins_backend.pin.repository.storyPin.StoryPinRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.hada.pins_backend.advice.ErrorCode.SERVER_ERROR;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/26.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService{
    private final MeetingPinRepositorySupport meetingPinRepositorySupport;
    private final CommunityPinRepositorySupport communityPinRepositorySupport;
    private final StoryPinRepositorySupport storyPinRepositorySupport;
    private final RetrofitInterface retrofitInterface;

    @Value("${kakao.key}")
    private String kakaoKey;

    @Value("${google.key}")
    String googleKey;


    @Override
    public List<HomeLocationResponse.LocationResponse> searchLocation(String keyword) {

        Call<Object> call = retrofitInterface.getLocations(kakaoKey, keyword);
        HomeLocationResponse homeLocationResponse = new HomeLocationResponse();
        try {
            Response<Object> response = call.execute();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            String json = new ObjectMapper().writeValueAsString(response.body());
            HomeLocationResponse getResult = objectMapper.readValue(json, HomeLocationResponse.class);
            if(Objects.nonNull(getResult))
                homeLocationResponse = getResult;
        } catch (IOException e) {
            throw new BusinessException(SERVER_ERROR);
        }

        return homeLocationResponse.getLocationResponses();
    }

    @Override
    public Map<String,List> loadPin(User user, HomePinRequest homePinRequest) {

        LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude(homePinRequest.getLatitude(), homePinRequest.getLongitude());

        //만남핀 가져오기
        List<MeetingPin> meetingPins = meetingPinRepositorySupport.findAllMeetingPinAtHome(
                longitudeAndLatitude,
                user,
                homePinRequest
        );

        //커뮤니티핀 가져오기
        List<CommunityPin> communityPins = communityPinRepositorySupport.findAllCommunityPinAtHome(
                longitudeAndLatitude,
                user,
                homePinRequest
        );

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepositorySupport.findAllStoryPinAtHome(
                longitudeAndLatitude,
                homePinRequest
        );
        GpsToAddress gpsToAddress = new GpsToAddress(googleKey);
        Map<String,List> stringListMap = new HashMap<>();
        stringListMap.put("meetingPin", meetingPins.stream()
                    .map(meetingPin -> MeetingPinCardViewResponse.toMeetingPinCardView(meetingPin,gpsToAddress))
                    .sorted((pin1, pin2) -> comparePins(pin1, pin2, longitudeAndLatitude))
                    .collect(Collectors.toList()));
        stringListMap.put("communityPin",communityPins.stream()
                    .map(CommunityPinCardViewResponse::toCommunityPinCardView)
                    .sorted((pin1,pin2) -> comparePins(pin1,pin2,longitudeAndLatitude))
                    .collect(Collectors.toList()));
        stringListMap.put("storyPin",storyPins.stream()
                    .map(StoryPinCardViewResponse::toStoryPinCardView)
                    .sorted((pin1,pin2) -> comparePins(pin1,pin2,longitudeAndLatitude))
                    .collect(Collectors.toList()));

        return stringListMap;
    }

    private static int comparePins(Object pin1, Object pin2, LongitudeAndLatitude longitudeAndLatitude){
        if(pin1 instanceof MeetingPinCardViewResponse){
            return (int) (distance(((MeetingPinCardViewResponse) pin1).getLatitude(),
                    ((MeetingPinCardViewResponse) pin1).getLongitude(),
                    longitudeAndLatitude,
                    "meter") -
                    distance(((MeetingPinCardViewResponse) pin2).getLatitude(),
                            ((MeetingPinCardViewResponse) pin2).getLongitude(),
                            longitudeAndLatitude,
                            "meter"));
        }else if(pin1 instanceof CommunityPinCardViewResponse){
            return (int) (distance(((CommunityPinCardViewResponse) pin1).getLatitude(),
                    ((CommunityPinCardViewResponse) pin1).getLongitude(),
                    longitudeAndLatitude,
                    "meter") -
                    distance(((CommunityPinCardViewResponse) pin2).getLatitude(),
                            ((CommunityPinCardViewResponse) pin2).getLongitude(),
                            longitudeAndLatitude,
                            "meter"));
        }else{
            return (int) (distance(((StoryPinCardViewResponse) pin1).getLatitude(),
                    ((StoryPinCardViewResponse) pin1).getLongitude(),
                    longitudeAndLatitude,
                    "meter") -
                    distance(((StoryPinCardViewResponse) pin2).getLatitude(),
                            ((StoryPinCardViewResponse) pin2).getLongitude(),
                            longitudeAndLatitude,
                            "meter"));
        }
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param longitudeAndLatitude 지점 2
     * @param unit 거리 표출단위
     * @return double 거리
     */
    private static double distance(double lat1, double lon1, LongitudeAndLatitude longitudeAndLatitude, String unit) {
        double lat2 = longitudeAndLatitude.getLatitude();
        double lon2 = longitudeAndLatitude.getLongitude();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit.equals("kilometer") ) {
            dist = dist * 1.609344;
        } else if(unit.equals("meter")){
            dist = dist * 1609.344;
        }

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
