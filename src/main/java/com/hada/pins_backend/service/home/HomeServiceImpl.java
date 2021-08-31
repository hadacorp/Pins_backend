package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
    private final MeetingPinRepository meetingPinRepository;
    private final CommunityPinRepository communityPinRepository;
    private final StoryPinRepository storyPinRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<HomePinResponse>> loadPin(String phoneNum, double latitude, double longitude) {
        double latitudeRange = 0.0075;
        double maxLatitude = latitude+ latitudeRange, minLatitude = latitude- latitudeRange;
        double longitudeRange = 0.004;
        double maxLongitude = longitude+ longitudeRange, minLongitude = longitude- longitudeRange;

        // 혹시 모르게 생길 토큰 오류 체크
        if(userRepository.findByPhoneNum(phoneNum).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        // 토큰에서 가져온 전화번호로 사용자 나이와 성별 가져오기
        int age = userRepository.findByPhoneNum(phoneNum).get().getAge();
        Gender gender =  userRepository.findByPhoneNum(phoneNum).get().getGender();

        //만남핀 가져오기
        List<MeetingPin> meetingPins =meetingPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqualAndMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude,
                age,
                age
        );

        //커뮤니티핀 가져오기
        List<CommunityPin> communityPins =communityPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqualAndMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude,
                age,
                age
        );
        List<HomePinResponse> homePinResponses = new ArrayList<>();

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude
        );

        System.out.println("<-------------->");

        //만남핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다.
        meetingPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
            homePinResponses.add(HomePinResponse.builder()
                    .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                    .pinType("meetingPin")
                    .latitude(pin.getLatitude())
                    .longitude(pin.getLongitude())
                    .pinDBId(pin.getId())
                    .category(pin.getCategory())
                    .build()
            );
        }
        });

        //커뮤니티 핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다.
        communityPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
            homePinResponses.add(HomePinResponse.builder()
                    .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                    .pinType("communityPins")
                    .latitude(pin.getLatitude())
                    .longitude(pin.getLongitude())
                    .pinDBId(pin.getId())
                    .category(pin.getCategory())
                    .build()
            );
        }
        });

        //이야기 핀에서 homePinResponses에 거리를 계산하여 넣어준다.
        storyPins.forEach((pin)-> homePinResponses.add(HomePinResponse.builder()
                .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                .pinType("storyPins")
                .latitude(pin.getLatitude())
                .longitude(pin.getLongitude())
                .pinDBId(pin.getId())
                .category(pin.getCategory())
                .build()
        ));

        homePinResponses.forEach(System.out::println);

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
