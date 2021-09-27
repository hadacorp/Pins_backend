package com.hada.pins_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import com.hada.pins_backend.service.user.UserServiceImpl;
import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PinControllerTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("커뮤니티 핀 생성 컨트롤러 mvc 테스트")
    void createCommunityPin() throws Exception{
        RequestCreateCommunityPin requestCreateCommunityPin = RequestCreateCommunityPin.builder()
                .title("아주대학교 태권도 커뮤니티")
                .content("태권도 하실분")
                .category("스포츠/운동")
                .setGender("Both")
                .minAge(20)
                .maxAge(30)
                .setLimit(10)
                .longitude(37.2761063)
                .latitude(127.0424111)
                .image(null).build();

        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilderUtils
                        .postForm("/pin/communitypin",requestCreateCommunityPin)
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("만남 핀 생성 컨트롤러 mvc 테스트")
    void createMeetingPin() throws Exception{
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

        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/pin/meetingpin")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .content(objectMapper.writeValueAsString(requestMeetingPin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("이야기 핀 생성 컨트롤러 mvc 테스트")
    void createStoryPin() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());
        RequestStoryPin requestStoryPin = RequestStoryPin.builder()
                .title("에어팟 케이스 분실")
                .content("정문에서 에어팟 케이스 분실 했는데 보신분?")
                .category("#분실/실종")
                .longitude(127.0465105)
                .latitude(37.2795816)
                .image(null)
                .build();

        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilderUtils
                        .postForm("/pin/storypin",requestStoryPin)
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("만남핀 가져오기 MVC 테스트")
    void getMeetingPin() throws Exception {
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        //존재하는 핀일때
        mockMvc.perform(MockMvcRequestBuilders.get("/pin/meetingpin/20")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        //존재하지 않는 핀 일때
        mockMvc.perform(MockMvcRequestBuilders.get("/pin/meetingpin/102000000")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("이야기핀 가져오기 MVC 테스트")
    void getStoryPin() throws Exception {
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        //존재하는 핀일때
        mockMvc.perform(MockMvcRequestBuilders.get("/pin/storypin/20")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        //존재하지 않는 핀 일때
        mockMvc.perform(MockMvcRequestBuilders.get("/pin/storypin/102000000")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
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