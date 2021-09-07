package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
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
    @BeforeEach
    public void insertPin() throws IOException {
        insertUser();
    }

    @Test
    @DisplayName("커뮤니티 핀 생성")
    void Test1() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());
        RequestCreateCommunityPin requestCreateCommunityPin = RequestCreateCommunityPin.builder()
                .title("아주대학교 태권도 커뮤니티")
                .content("태권도 하실분")
                .category("스포츠/운동")
                .setGender("Both")
                .minAge(20)
                .maxAge(30)
                .setLimit(10)
                .longitude(37.2761066)
                .latitude(127.0424131)
                .image(file).build();

        pinService.createCommunityPin(userRepository.findAll().get(0),requestCreateCommunityPin);
        communityPinRepository.findAll().forEach(System.out::println);

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