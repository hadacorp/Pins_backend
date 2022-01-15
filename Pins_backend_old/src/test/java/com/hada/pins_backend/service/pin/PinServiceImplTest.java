package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.home.HomeServiceImpl;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@SpringBootTest
class PinServiceImplTest {
    @Autowired
    private PinServiceImpl pinService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommunityPinRepository communityPinRepository;
    @Autowired
    private MeetingPinRepository meetingPinRepository;
    @Autowired
    private StoryPinRepository storyPinRepository;

    @Test
    @DisplayName("커뮤니티 핀 생성")
    void Test1() throws Exception{
        String url = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.27.png";
        MockMultipartFile file = new MockMultipartFile("file","userimage0.png" , "image/png" ,new URL(url).openStream());

        RequestCreateCommunityPin requestCreateCommunityPin = RequestCreateCommunityPin.builder()
                .title("아주대학교 태권도 커뮤니티")
                .content("태권도 하실분")
                .category("스포츠/운동")
                .setGender("Both")
                .detail("detaile입니다")
                .minAge(20)
                .participationType(0)
                .profileType(0)
                .maxAge(30)
                .latitude(37.2761066)
                .longitude(127.0424131)
                .image(file).build();

        pinService.createCommunityPin(userRepository.findAll().get(0), requestCreateCommunityPin);
        communityPinRepository.findAll().forEach(System.out::println);


    }

    @Test
    @DisplayName("만남 핀 생성")
    void Test2(){
        RequestMeetingPin requestMeetingPin = RequestMeetingPin.builder()
                .title("아주대 앞 카공")
                .content("유메야에서 닥공")
                .category("스터디/독서")
                .setGender("Both")
                .minAge(20)
                .maxAge(24)
                .setLimit(3)
                .longitude(127.0446612)
                .latitude(37.5519156)
                .date(3L)
                .hour(13)
                .minute(30)
                .build();

        pinService.createMeetingPin(userRepository.findAll().get(0),requestMeetingPin);
        meetingPinRepository.findAll().forEach(System.out::println);
    }

    @Test
    @DisplayName("이야기 핀 생성")
    void Test3() throws Exception{
        RequestStoryPin requestStoryPin = RequestStoryPin.builder()
                .title("에어팟 케이스 분실")
                .content("정문에서 에어팟 케이스 분실 했는데 보신분?")
                .category("#분실/실종")
                .longitude(127.0465105)
                .latitude(37.2795816)
                .image(null)
                .build();

        pinService.createStoryPin(userRepository.findAll().get(0),requestStoryPin);
        storyPinRepository.findAll().forEach(System.out::println);
    }

//    @Test
//    @DisplayName("만남핀 가져오기")
//    void Test4(){
//        System.out.println(pinService.getMeetingPin(12L));
//    }

    @Test
    @DisplayName("이야기핀 가져오기")
    void Test5(){
        System.out.println(pinService.getStoryPin(12L));
    }

    private void insertUser() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());

        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                file);
        userService.insertUser(joinUserRequest);
    }


}