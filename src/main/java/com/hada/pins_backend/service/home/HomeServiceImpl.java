package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.*;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.home.FilterData;
import com.hada.pins_backend.dto.home.LongitudeAndLatitude;
import com.hada.pins_backend.dto.home.response.HomeCardViewResponse;
import com.hada.pins_backend.dto.home.response.HomeLocationResponse;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import com.hada.pins_backend.exception.home.PintypeDBIdException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
    private final MeetingPinRepository meetingPinRepository;
    private final StoryPinRepository storyPinRepository;
    private final UserRepository userRepository;
    private final StoryPinLikeRepository storyPinLikeRepository;
    private final StoryPinCommentRepository storyPinCommentRepository;
    @Value("${kakao.key}")
    private String kakaoKey;

    /*

      핀, 카드뷰 로딩 시나리오
      1. 검색 좌표, 클릭핀 위도, 경도 계산
      2. 토큰으로 유저 정보 get
      3. JPA로 핀들을 할 수 있는데로 걸러서 가져오기
      4. params로 받아온 필터 정보들을 if문에 넣어 사용하기 좋도록 rename 하기
      5. 필터 조건에 맞는 핀들을 걸러 response 형식에 맞게 넣기
      6. 정렬이 필요하면 정렬
      7. Return ResponseEntity

     */

    /**
     * 홈화면 핀 로딩
     */
    @Override
    public ResponseEntity<List<HomePinResponse>> loadPin(String phoneNum, LongitudeAndLatitude longitudeAndLatitude, FilterData filterData) {
        //최대 최소 위도 경도 계산
        double maxLatitude = longitudeAndLatitude.getMaxLatitude(), minLatitude = longitudeAndLatitude.getMinLatitude();
        double maxLongitude = longitudeAndLatitude.getMaxLongitude(), minLongitude = longitudeAndLatitude.getMinLongitude();


        // 혹시 모르게 생길 토큰 오류 체크
        if(userRepository.findByPhoneNum(phoneNum).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        // 토큰에서 가져온 전화번호로 사용자 나이와 성별 가져오기
        int age = userRepository.findByPhoneNum(phoneNum).get().getAge();
        Gender gender =  userRepository.findByPhoneNum(phoneNum).get().getGender();

        //만남핀 가져오기
        List<MeetingPin> meetingPins = meetingPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqualAndMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude,
                age,
                age
        );


        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude
        );


        List<HomePinResponse> homePinResponses = new ArrayList<>();

        //만남핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{
            if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
                if (filterData.meetingPinFilter(pin)){
                    homePinResponses.add(HomePinResponse.builder()
                            .pinType("meetingPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .build()
                    );
                }
            }
        });

        //이야기 핀에서 homePinResponses에 거리를 계산하여 넣어준다.
        storyPins.forEach((pin)-> {
            if(filterData.storyPinFilter(pin)){
                homePinResponses.add(HomePinResponse.builder()
                        .pinType("storyPin")
                        .latitude(pin.getLatitude())
                        .longitude(pin.getLongitude())
                        .pinDBId(pin.getId())
                        .category(pin.getCategory())
                        .build()
                );
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(homePinResponses);
    }


    /**
     * 키워드 핀 검색
     */
    @Override
    public ResponseEntity<List<HomePinResponse>> searchPin(String phoneNum, String keyword, LongitudeAndLatitude longitudeAndLatitude,FilterData filterData) {
        log.info("keyword serach ::=> {}, {}, {} ",longitudeAndLatitude.getLatitude(),longitudeAndLatitude.getLongitude(),keyword);
        // 혹시 모르게 생길 토큰 오류 체크
        if(userRepository.findByPhoneNum(phoneNum).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        // 토큰에서 가져온 전화번호로 사용자 나이와 성별 가져오기
        int age = userRepository.findByPhoneNum(phoneNum).get().getAge();
        Gender gender =  userRepository.findByPhoneNum(phoneNum).get().getGender();

        //만남핀 가져오기
        List<MeetingPin> meetingPins =meetingPinRepository.findByMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(age,age);

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByTitleContainingOrContentContaining(keyword,keyword);


        List<HomePinResponse> homePinResponses = new ArrayList<>();

        // 만남핀에서 성별조건 확인후 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
//            log.info("keyword {} {}", pin.getTitle().contains(keyword), pin.getContent().contains(keyword));
            if (pin.getTitle().contains(keyword) || pin.getContent().contains(keyword)) {
                if (filterData.meetingPinFilter(pin)){
                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(),"meter"))
                            .pinType("meetingPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .build()
                    );
                }
            }
        }
        });

        //이야기 핀에서 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        storyPins.forEach((pin)-> {
//            log.info("{}",pin);
            if(filterData.storyPinFilter(pin)){
                homePinResponses.add(HomePinResponse.builder()
                        .distance(distance(pin.getLatitude(), pin.getLongitude(), longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(),"meter"))
                        .pinType("storyPin")
                        .latitude(pin.getLatitude())
                        .longitude(pin.getLongitude())
                        .pinDBId(pin.getId())
                        .category(pin.getCategory())
                        .build()
                );

            }
        });

        Collections.sort(homePinResponses);
        List<HomePinResponse> subHomePinResponses;
        if(homePinResponses.size()>100) {
            subHomePinResponses = homePinResponses.subList(0, 100);
            return ResponseEntity.status(HttpStatus.OK).body(subHomePinResponses);
        }else return ResponseEntity.status(HttpStatus.OK).body(homePinResponses);
    }

    /**
     * 카드뷰 로딩
     */
    @Override
    @Transactional
    public ResponseEntity<List<HomeCardViewResponse>> loadCardView(String phoneNum, String pinType, Long pinDBId, FilterData filterData) throws RuntimeException{

        LongitudeAndLatitude longitudeAndLatitude = getLongitudeAndLatitude(pinType,pinDBId);

        //최대 최소 위도 경도 계산
        double maxLatitude = longitudeAndLatitude.getMaxLatitude(), minLatitude = longitudeAndLatitude.getMinLatitude();
        double maxLongitude = longitudeAndLatitude.getMaxLongitude(), minLongitude = longitudeAndLatitude.getMinLongitude();

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


        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude
        );


        List<HomeCardViewResponse> homeCardViewResponses = new ArrayList<>();

        // 클릭한 핀을 맨 앞으로 이동
        homeCardViewResponses.add(getFirstPinData(pinType,pinDBId,longitudeAndLatitude.getLatitude(),longitudeAndLatitude.getLongitude()));

        //만남핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{
            if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
                if (filterData.meetingPinFilter(pin)){
                    if(!(pinType.equals("meetingPin") && pinDBId.equals(pin.getId()))) {

                        // 날짜 핀카드 형식대로 받아오기
                        LocalDateTime getdate = pin.getDate();
                        int renameHour = getRenameHour(getdate);
                        String amPm = getAmPm(getdate);
                        DayOfWeek dayOfWeek = getdate.getDayOfWeek();

                        homeCardViewResponses.add(HomeCardViewResponse.builder()
                                .distance(distance(pin.getLatitude(), pin.getLongitude(),longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(), "meter"))
                                .pinType("meetingPin")
                                .latitude(pin.getLatitude())
                                .longitude(pin.getLongitude())
                                .pinDBId(pin.getId())
                                .category(pin.getCategory())
                                .title(pin.getTitle())
                                .image(pin.getCreateUser().getProfileImage())
                                .date(getdate.format(DateTimeFormatter.ofPattern("M월 dd일 (")) + dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN) + ") " + amPm + renameHour + "시")
                                .build()
                        );
                    }
                }
            }
        });

        //이야기 핀에서 homePinResponses에 거리를 계산하여 넣어준다.
        storyPins.forEach((pin)-> {
            if(filterData.storyPinFilter(pin)){
                if(!(pinType.equals("storyPin") && pinDBId.equals(pin.getId()))) {
                    int likes = storyPinLikeRepository.findStoryPinLikesByStoryPin_Id(pin.getId()).size();
                    int comments = storyPinCommentRepository.findStoryPinCommentsByStoryPin_Id(pin.getId()).size();
                    homeCardViewResponses.add(HomeCardViewResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(), "meter"))
                            .pinType("storyPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .comment(comments)
                            .like(likes)
                            .image(pin.getImage())
                            .build()
                    );
                }
            }
        });
        Collections.sort(homeCardViewResponses);

        if(homeCardViewResponses.size()>50) {
            List<HomeCardViewResponse> subhomeCardViewResponses;
            subhomeCardViewResponses = homeCardViewResponses.subList(0, 50);
            return ResponseEntity.status(HttpStatus.OK).body(subhomeCardViewResponses);
        }else return ResponseEntity.status(HttpStatus.OK).body(homeCardViewResponses);

    }
    /**
     * 홈화면 키워드 카드뷰 로딩
     */
    @Override
    public ResponseEntity<List<HomeCardViewResponse>> searchCardView(String phoneNum, String pinType, Long pinDBId, String keyword, FilterData filterData) {
        LongitudeAndLatitude longitudeAndLatitude = getLongitudeAndLatitude(pinType,pinDBId);

        // 혹시 모르게 생길 토큰 오류 체크
        if(userRepository.findByPhoneNum(phoneNum).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        // 토큰에서 가져온 전화번호로 사용자 나이와 성별 가져오기
        int age = userRepository.findByPhoneNum(phoneNum).get().getAge();
        Gender gender =  userRepository.findByPhoneNum(phoneNum).get().getGender();

        //만남핀 가져오기
        List<MeetingPin> meetingPins =meetingPinRepository.findByMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(age,age);

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByTitleContainingOrContentContaining(keyword,keyword);

        List<HomeCardViewResponse> homeCardViewResponses = new ArrayList<>();

        // 클릭한 핀을 맨 앞으로 이동
        homeCardViewResponses.add(getFirstPinData(pinType,pinDBId,longitudeAndLatitude.getLatitude(),longitudeAndLatitude.getLongitude()));

        // 만남핀에서 성별조건 확인후 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
            if (pin.getTitle().contains(keyword) || pin.getContent().contains(keyword)) {
                if (filterData.meetingPinFilter(pin)){
                    if(!(pinType.equals("meetingPin") && pinDBId.equals(pin.getId()))) {
                        // 날짜 핀카드 형식대로 받아오기
                        LocalDateTime getdate = pin.getDate();
                        int renameHour = getRenameHour(getdate);
                        String amPm = getAmPm(getdate);
                        DayOfWeek dayOfWeek = getdate.getDayOfWeek();

                        homeCardViewResponses.add(HomeCardViewResponse.builder()
                                .distance(distance(pin.getLatitude(), pin.getLongitude(), longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(),"meter"))
                                .pinType("meetingPin")
                                .latitude(pin.getLatitude())
                                .longitude(pin.getLongitude())
                                .pinDBId(pin.getId())
                                .category(pin.getCategory())
                                .title(pin.getTitle())
                                .image(pin.getCreateUser().getProfileImage())
                                .date(getdate.format(DateTimeFormatter.ofPattern("MM월 dd일 ("))+dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)+") "+ amPm + renameHour+"시")
                                .build()
                        );
                    }
                }
            }
        }
        });

        //이야기 핀에서 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        storyPins.forEach((pin)-> {
            if(filterData.storyPinFilter(pin)){
                if(!(pinType.equals("storyPin") && pinDBId.equals(pin.getId()))) {
                    int likes = storyPinLikeRepository.findStoryPinLikesByStoryPin_Id(pin.getId()).size();
                    int comments = storyPinCommentRepository.findStoryPinCommentsByStoryPin_Id(pin.getId()).size();
                    homeCardViewResponses.add(HomeCardViewResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), longitudeAndLatitude.getLatitude(), longitudeAndLatitude.getLongitude(), "meter"))
                            .pinType("storyPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .comment(comments)
                            .image(pin.getImage())
                            .like(likes)
                            .build()
                    );
                }
            }
        });

        Collections.sort(homeCardViewResponses);
        List<HomeCardViewResponse> subHomeCardViewResponse;
        if(homeCardViewResponses.size()>50) {
            subHomeCardViewResponse = homeCardViewResponses.subList(0, 50);
            return ResponseEntity.status(HttpStatus.OK).body(subHomeCardViewResponse);
        }else return ResponseEntity.status(HttpStatus.OK).body(homeCardViewResponses);
    }

    /**
     * 키워드 장소 검색
     */
    @Override
    public ResponseEntity<List<HomeLocationResponse>> searchLocation(String keyword) {

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
        JSONObject responseJson = new JSONObject(result.getBody());
        List<HomeLocationResponse> homeLocationResponses = new ArrayList<>();
        try {
            JSONArray locations = responseJson.getJSONArray("documents");
            for (int i = 0; i < locations.length(); i++) {
                JSONObject data = locations.getJSONObject(i);

                HomeLocationResponse homeLocationResponse = HomeLocationResponse.builder()
                        .placeName(data.getString("place_name"))
                        .longitude(data.getDouble("x"))
                        .latitude(data.getDouble("y"))
                        .build();
                homeLocationResponses.add(homeLocationResponse);
            }
        }catch (JSONException e){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(homeLocationResponses);

    }

    /**
     * 클릭핀 검증 및 위도,경도 가져오기
     */
    public LongitudeAndLatitude getLongitudeAndLatitude (String pinType, Long pinDBId) throws PintypeDBIdException{
        if (pinType.equals("storyPin")) {
            if (storyPinRepository.findById(pinDBId).isPresent()) {
                StoryPin storyPin = storyPinRepository.findById(pinDBId).get();
                return new LongitudeAndLatitude(storyPin.getLatitude(),storyPin.getLongitude());
            }else throw new PintypeDBIdException();
        }else{
            if (meetingPinRepository.findById(pinDBId).isPresent()) {
                MeetingPin meetingPin = meetingPinRepository.findById(pinDBId).get();
                return new LongitudeAndLatitude(meetingPin.getLatitude(),meetingPin.getLongitude());
            }else throw new PintypeDBIdException();
        }
    }
    /**
     * 클릭핀 첫번째에 넣기 위해 정보 가져오기
     */
    public HomeCardViewResponse getFirstPinData (String pinType, Long pinDBId, double latitude, double longitude) throws PintypeDBIdException {
        if (pinType.equals("storyPin")) {
            if (storyPinRepository.findById(pinDBId).isPresent()) {
                StoryPin storyPin = storyPinRepository.findById(pinDBId).get();
                int likes = storyPinLikeRepository.findStoryPinLikesByStoryPin_Id(storyPin.getId()).size();
                int comments = storyPinCommentRepository.findStoryPinCommentsByStoryPin_Id(storyPin.getId()).size();
                return HomeCardViewResponse.builder()
                        .distance(distance(storyPin.getLatitude(), storyPin.getLongitude(), latitude, longitude, "meter"))
                        .pinType("storyPin")
                        .latitude(storyPin.getLatitude())
                        .longitude(storyPin.getLongitude())
                        .pinDBId(storyPin.getId())
                        .category(storyPin.getCategory())
                        .title(storyPin.getTitle())
                        .comment(comments)
                        .image(storyPin.getImage())
                        .like(likes)
                        .build();
            }else throw new PintypeDBIdException();

        }else{
            if (meetingPinRepository.findById(pinDBId).isPresent()) {
                MeetingPin meetingPin = meetingPinRepository.findById(pinDBId).get();
                // 날짜 핀카드 형식대로 받아오기
                LocalDateTime getdate = meetingPin.getDate();
                int renameHour = getRenameHour(getdate);
                String amPm = getAmPm(getdate);
                DayOfWeek dayOfWeek = getdate.getDayOfWeek();
                return HomeCardViewResponse.builder()
                        .distance(distance(meetingPin.getLatitude(), meetingPin.getLongitude(), latitude, longitude, "meter"))
                        .pinType("meetingPin")
                        .latitude(meetingPin.getLatitude())
                        .longitude(meetingPin.getLongitude())
                        .pinDBId(meetingPin.getId())
                        .category(meetingPin.getCategory())
                        .title(meetingPin.getTitle())
                        .image(meetingPin.getCreateUser().getProfileImage())
                        .date(getdate.format(DateTimeFormatter.ofPattern("M월 dd일 (")) + dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN) + ") " + amPm + renameHour + "시")
                        .build();
            }else throw new PintypeDBIdException();
        }
    }

    /**
     * 오전 오후 문자열 가져오기
     */
    public String getAmPm (LocalDateTime date){
        if (date.getHour() > 12) {
            return "오후 ";
        }else return "오전 ";
    }

    /**
     * Hour 24시-> 12시 기준변경
     */
    public int getRenameHour (LocalDateTime date){
        if (date.getHour() > 12) {
            return date.getHour()-12;
        }else return date.getHour();
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