package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.MeetingPin;
import com.hada.pins_backend.pin.model.enumable.PinType;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import com.hada.pins_backend.pin.model.response.HomePinResponse;
import com.hada.pins_backend.pin.repository.MeetingPinRepository;
import com.hada.pins_backend.pin.repository.MeetingPinRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{
    private final MeetingPinRepositorySupport meetingPinRepositorySupport;

    @Value("${kakao.key}")
    private String kakaoKey;

    @Override
    public ResponseEntity<ApiResponse<List<HomeLocationResponse>>> searchLocation(String keyword) {
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

        try {
            JSONObject responseJson = new JSONObject(result.getBody());
            JSONArray locations = responseJson.getJSONArray("documents");
            for (Object jsonArray: locations) {
                JSONObject data = (JSONObject) jsonArray;

                HomeLocationResponse homeLocationResponse = HomeLocationResponse.builder()
                        .placeName(data.getString("place_name"))
                        .longitude(data.getDouble("x"))
                        .latitude(data.getDouble("y"))
                        .build();
                homeLocationResponses.add(homeLocationResponse);
            }
        }catch (JSONException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(homeLocationResponses));
    }

    @Override
    public ResponseEntity<ApiResponse<List<HomePinResponse>>> loadPin(User user, HomePinRequest homePinRequest) {

        LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude(homePinRequest.getLongitude(), homePinRequest.getLatitude());
        //최대 최소 위도 경도 계산
        double maxLatitude = longitudeAndLatitude.getMaxLatitude(), minLatitude = longitudeAndLatitude.getMinLatitude();
        double maxLongitude = longitudeAndLatitude.getMaxLongitude(), minLongitude = longitudeAndLatitude.getMinLongitude();


        // 토큰에서 가져온 전화번호로 사용자 나이와 성별 가져오기
        int age = user.getAge();
        Gender gender =  user.getGender();

        List<HomePinResponse> homePinResponses = new ArrayList<>();

        //만남핀 가져오기
        List<MeetingPin> meetingPins = meetingPinRepositorySupport.findAllMeetingPinAtHome(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude,
                age,
                gender
        );

        meetingPins.forEach(meetingPin -> {
            homePinResponses.add(HomePinResponse.builder()
                    .pinType(PinType.MeetingPin)
                    .distance(distance(
                            meetingPin.getLatitude(),
                            meetingPin.getLongitude(),
                            longitudeAndLatitude.getLatitude(),
                            longitudeAndLatitude.getLongitude(),
                            "meter"))
                    .pin(meetingPin)
                    .build());
        });

        Collections.sort(homePinResponses);

        return ResponseEntity.status(HttpStatus.OK).body(null);
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
