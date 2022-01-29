package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.MeetingPin;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import com.hada.pins_backend.pin.model.response.HomePinResponse;
import com.hada.pins_backend.pin.model.response.MeetingPinResponse;
import com.hada.pins_backend.pin.repository.MeetingPinRepositorySupport;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService{
    private final MeetingPinRepositorySupport meetingPinRepositorySupport;

    @Value("${kakao.key}")
    private String kakaoKey;

    @Value("${google.key}")
    private String googleKey;

    @Override
    public List<HomeLocationResponse> searchLocation(String keyword) throws ParseException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("Authorization",kakaoKey)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req,String.class);
        List<HomeLocationResponse> homeLocationResponses = new ArrayList<>();


        JSONParser jsonParser = new JSONParser();
        JSONObject responseJson = (JSONObject) jsonParser.parse(result.getBody());
        JSONArray locations = (JSONArray) responseJson.get("documents");
        for (Object jsonArray: locations) {
            JSONObject data = (JSONObject) jsonArray;

            HomeLocationResponse homeLocationResponse = HomeLocationResponse.builder()
                    .placeName((String) data.get("place_name"))
                    .longitude((Double) data.get("x"))
                    .latitude((Double) data.get("y"))
                    .build();
            homeLocationResponses.add(homeLocationResponse);
        }

        return homeLocationResponses;
    }

    @Override
    public List<HomePinResponse> loadPin(User user, HomePinRequest homePinRequest) {

        LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude(homePinRequest.getLongitude(), homePinRequest.getLatitude());

        int age = user.getAge();
        Gender gender =  user.getGender();

        List<HomePinResponse> homePinResponses = new ArrayList<>();

        //만남핀 가져오기
        List<MeetingPin> meetingPins = meetingPinRepositorySupport.findAllMeetingPinAtHome(
                longitudeAndLatitude,
                age,
                gender
        );

        meetingPins.forEach(meetingPin -> {
            MeetingPinResponse meetingPinResponse = new MeetingPinResponse();
            homePinResponses.add(HomePinResponse.builder()
                    .pinType(HomePinResponse.PinType.MeetingPin)
                    .distance(distance(
                            meetingPin.getLatitude(),
                            meetingPin.getLongitude(),
                            longitudeAndLatitude.getLatitude(),
                            longitudeAndLatitude.getLongitude(),
                            "meter"))
                    .pin(meetingPinResponse.meetingPin2Response(meetingPin,googleKey))
                    .build());
        });

        Collections.sort(homePinResponses);

        return null;
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return double 거리
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

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
