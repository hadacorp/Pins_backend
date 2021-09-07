package com.hada.pins_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
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

    @BeforeEach
    public void insertPin() throws IOException {
        insertUser();
    }



    @Test
    @DisplayName("커뮤니티 핀 생성 컨트롤러 mvc 테스트")
    void join() throws Exception{
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