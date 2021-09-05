package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.home.response.HomeLocationResponse;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
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
import java.nio.charset.StandardCharsets;
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
    private final CommunityPinRepository communityPinRepository;
    private final StoryPinRepository storyPinRepository;
    private final UserRepository userRepository;
    @Value("${kakao.key}")
    private String kakaoKey;
    /**
     * 홈화면 핀 로딩
     */
    @Override
    public ResponseEntity<List<HomePinResponse>> loadPin(String phoneNum, double latitude, double longitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory) {
        //최대 최소 위도 경도 계산
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

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(
                maxLatitude,
                minLatitude,
                maxLongitude,
                minLongitude
        );

        // 필터 정보를 구별하기 쉽게 변수 선언
        String renameGender, renameDate  ,renameMeetingCategory, renameCommunityCategory, renameStoryCategory;
        int minAge, maxAge, minTime,maxTime;

        //상대방 성별 필터 rename
        if(meetGender.equals("all")) renameGender = "Male-Female";
        else renameGender = meetGender;

        //날짜 필터 rename
        LocalDate now = LocalDate.now();
        StringBuilder date = new StringBuilder();
        if(meetDate.equals("all")){
            for(int i =0;i<8;i++){
                date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                now = now.plusDays(1);
            }
            renameDate = date.toString();
        }else{
            for(int i =0;i<8;i++){
                if(meetDate.contains(String.valueOf(i))){
                    date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                }
                now = now.plusDays(1);
            }
            renameDate = date.toString();
        }

        // 상대방 나이 필터 rename
        if(meetAge.equals("all")){
            minAge = 20;
            maxAge = 100;
        }else{
            StringTokenizer st = new StringTokenizer(meetAge,"-");
            minAge = Integer.parseInt(st.nextToken());
            maxAge = Integer.parseInt(st.nextToken());
        }

        // 시간 필터 rename
        if(meetTime.equals("all")){
            minTime = 0;
            maxTime = 24;
        }else{
            StringTokenizer st = new StringTokenizer(meetTime,"-");
            minTime = Integer.parseInt(st.nextToken());
            maxTime = Integer.parseInt(st.nextToken());
        }
        //각 핀별 카테고리 rename
        String allMeetingPinCategory = "대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-거래/나눔-기타";
        if(meetingPinCategory.equals("all")) renameMeetingCategory = allMeetingPinCategory;
        else renameMeetingCategory = meetingPinCategory;

        String allCommunityPinCategory = "학교/동창-아파트/이웃-대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-기타";
        if(communityPinCategory.equals("all")) renameCommunityCategory = allCommunityPinCategory;
        else renameCommunityCategory = communityPinCategory;

        String allStoryPinCategory = "#궁금해요#장소리뷰#동네꿀팁#반려동물#취미생횔#도와줘요#사건시고#분실/실종#잡담";
        if(storyPinCategory.equals("all")) renameStoryCategory = allStoryPinCategory;
        else renameStoryCategory = storyPinCategory;

        List<HomePinResponse> homePinResponses = new ArrayList<>();

        //만남핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{
            if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
                if (renameMeetingCategory.contains(pin.getCategory())&&
                        renameGender.contains(pin.getCreateUser().getGender().toString())&&
                        pin.getCreateUser().getAge() >=minAge && pin.getCreateUser().getAge() <= maxAge &&
                        pin.getDate().getHour()>=minTime && pin.getDate().getHour()<=maxTime &&
                        renameDate.contains(pin.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){

                    // 날짜 핀카드 형식대로 받아오기
                    LocalDateTime getdate = pin.getDate();
                    int renameHour = getdate.getHour();
                    String amPm = "오전 ";
                    if (getdate.getHour() > 12) {
                        amPm = "오후 ";
                        renameHour -= 12;
                    }
                    DayOfWeek dayOfWeek = getdate.getDayOfWeek();

                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                            .pinType("meetingPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .image(pin.getCreateUser().getImage())
                            .date(getdate.format(DateTimeFormatter.ofPattern("M월 dd일 ("))+dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)+") "+ amPm + renameHour+"시")
                            .build()
                    );
                }
            }
        });

        //커뮤니티 핀에서 성별조건 확인후 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        communityPins.forEach((pin)->{
            if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
                if(renameCommunityCategory.contains(pin.getCategory())){
                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                            .pinType("communityPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .image(pin.getImage())
                            .build()
                    );
                }
            }
        });

        //이야기 핀에서 homePinResponses에 거리를 계산하여 넣어준다.
        storyPins.forEach((pin)-> {
            if(renameStoryCategory.contains(pin.getCategory())){
                homePinResponses.add(HomePinResponse.builder()
                        .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                        .pinType("storyPin")
                        .latitude(pin.getLatitude())
                        .longitude(pin.getLongitude())
                        .pinDBId(pin.getId())
                        .category(pin.getCategory())
                        .title(pin.getTitle())
                        .build()
                );
            }
        });

        Collections.sort(homePinResponses);

        return ResponseEntity.status(HttpStatus.OK).body(homePinResponses);
    }


    /**
     * 키워드 핀 검색
     */
    @Override
    public ResponseEntity<List<HomePinResponse>> searchPin(String phoneNum, String keyword, double latitude, double longitude, String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory) {
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
        List<MeetingPin> meetingPins =meetingPinRepository.findByMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(age,age);

        //커뮤니티핀 가져오기
        List<CommunityPin> communityPins =communityPinRepository.findAll();

        //이야기핀 가져오기
        List<StoryPin> storyPins = storyPinRepository.findAll();

        // 필터 정보를 구별하기 쉽게 변수 선언
        String renameGender, renameDate  ,renameMeetingCategory, renameCommunityCategory, renameStoryCategory;
        int minAge, maxAge, minTime,maxTime;

        //상대방 성별 필터 rename
        if(meetGender.equals("all")) renameGender = "Male-Female";
        else renameGender = meetGender;

        //날짜 필터 rename
        LocalDate now = LocalDate.now();
        StringBuilder date = new StringBuilder();
        if(meetDate.equals("all")){
            for(int i =0;i<8;i++){
                date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                now = now.plusDays(1);
            }
            renameDate = date.toString();
        }else{
            for(int i =0;i<8;i++){
                if(meetDate.contains(String.valueOf(i))){
                    date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                }
                now = now.plusDays(1);
            }
            renameDate = date.toString();
        }

        // 상대방 나이 필터 rename
        if(meetAge.equals("all")){
            minAge = 20;
            maxAge = 100;
        }else{
            StringTokenizer st = new StringTokenizer(meetAge,"-");
            minAge = Integer.parseInt(st.nextToken());
            maxAge = Integer.parseInt(st.nextToken());
        }

        // 시간 필터 rename
        if(meetTime.equals("all")){
            minTime = 0;
            maxTime = 24;
        }else{
            StringTokenizer st = new StringTokenizer(meetTime,"-");
            minTime = Integer.parseInt(st.nextToken());
            maxTime = Integer.parseInt(st.nextToken());
        }

        //각 핀별 카테고리 rename
        String allMeetingPinCategory = "대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-거래/나눔-기타";
        if(meetingPinCategory.equals("all")) renameMeetingCategory = allMeetingPinCategory;
        else renameMeetingCategory = meetingPinCategory;

        String allCommunityPinCategory = "학교/동창-아파트/이웃-대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-기타";
        if(communityPinCategory.equals("all")) renameCommunityCategory = allCommunityPinCategory;
        else renameCommunityCategory = communityPinCategory;

        String allStoryPinCategory = "#궁금해요#장소리뷰#동네꿀팁#반려동물#취미생횔#도와줘요#사건시고#분실/실종#잡담";
        if(storyPinCategory.equals("all")) renameStoryCategory = allStoryPinCategory;
        else renameStoryCategory = storyPinCategory;

        List<HomePinResponse> homePinResponses = new ArrayList<>();
        log.info("meetingPins {}",meetingPins);
        // 만남핀에서 성별조건 확인후 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        meetingPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
            log.info("keyword {} {}", pin.getTitle().contains(keyword), pin.getContent().contains(keyword));
            if (pin.getTitle().contains(keyword) || pin.getContent().contains(keyword)) {

                //필터링 테스트 로그 코드
                log.info("renameMeetingCategory {}",renameMeetingCategory.contains(pin.getCategory()));
                log.info("renameGender {}",renameGender.contains(pin.getCreateUser().getGender().toString()));
                log.info("getAge {} {}",pin.getCreateUser().getAge() >=minAge,pin.getCreateUser().getAge() <= maxAge);
                log.info("getDate {} {}", pin.getDate().getHour()>=minTime,pin.getDate().getHour()<=maxTime);
                log.info("renameDate {}",renameDate.contains(pin.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                if (renameMeetingCategory.contains(pin.getCategory())&&
                        renameGender.contains(pin.getCreateUser().getGender().toString())&&
                        pin.getCreateUser().getAge() >=minAge && pin.getCreateUser().getAge() <= maxAge &&
                        pin.getDate().getHour()>=minTime && pin.getDate().getHour()<=maxTime &&
                        renameDate.contains(pin.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){

                    // 날짜 핀카드 형식대로 받아오기
                    LocalDateTime getdate = pin.getDate();
                    int renameHour = getdate.getHour();
                    String amPm = "오전 ";
                    if (getdate.getHour() > 12) {
                        amPm = "오후 ";
                        renameHour -= 12;
                    }
                    DayOfWeek dayOfWeek = getdate.getDayOfWeek();

                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                            .pinType("meetingPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .image(pin.getCreateUser().getImage())
                            .date(getdate.format(DateTimeFormatter.ofPattern("MM월 dd일 ("))+dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)+") "+ amPm + renameHour+"시")
                            .build()
                    );
                }
            }
        }
        });

        //커뮤니티 핀에서 성별조건 확인후 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        communityPins.forEach((pin)->{if (pin.getSetGender() == gender || pin.getSetGender() ==Gender.Both) {
            if (pin.getTitle().contains(keyword) || pin.getContent().contains(keyword)) {
                if(renameCommunityCategory.contains(pin.getCategory())){
                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                            .pinType("communityPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .image(pin.getImage())
                            .build()
                    );
                }
            }
        }
        });

        //이야기 핀에서 키워드가 포함된 핀만 homePinResponses에 거리를 계산하여 넣어준다. + 필터 적용
        storyPins.forEach((pin)-> {
            if (pin.getTitle().contains(keyword) || pin.getContent().contains(keyword)) {
                if(renameStoryCategory.contains(pin.getCategory())){
                    homePinResponses.add(HomePinResponse.builder()
                            .distance(distance(pin.getLatitude(), pin.getLongitude(), latitude, longitude,"meter"))
                            .pinType("storyPin")
                            .latitude(pin.getLatitude())
                            .longitude(pin.getLongitude())
                            .pinDBId(pin.getId())
                            .category(pin.getCategory())
                            .title(pin.getTitle())
                            .build()
                    );
                }
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
                        .x(data.getDouble("x"))
                        .y(data.getDouble("y"))
                        .build();
                homeLocationResponses.add(homeLocationResponse);
            }
        }catch (JSONException e){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(homeLocationResponses);

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
