package com.hada.pins_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.dto.user.NicknameDto;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserService;
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
import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
//@WebMvcTest(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @BeforeEach
    void insertOne() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());

        JoinUserRequest joinUserRequest = new JoinUserRequest("아무개",
                "아아무무개",
                "011212-2",
                "010-1234-5678",
                file);
        userService.insertUser(joinUserRequest);
    }

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    void join() throws Exception{
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                null);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/join")
                        .content(objectMapper.writeValueAsString(joinUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 컨트롤러 오류 테스트 -형식 오류")
    void errorJoin() throws Exception{
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅",
                "9801031",
                "010-7760-6393",
                null);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/join")
                        .content(objectMapper.writeValueAsString(joinUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("가입여부 확인 컨트롤러 테스트")
    void oldUser() throws Exception {
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("010-7760-6393").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/old-user")
                        .content(objectMapper.writeValueAsString(userLoginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(print());
    }
    @Test
    @DisplayName("가입여부 확인 컨트롤러 오류 테스트 - 형식 오류")
    void errorOldUser() throws Exception {
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("0107760-6393").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/old-user")
                        .content(objectMapper.writeValueAsString(userLoginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 중복 여부 컨트롤러 테스트")
    void nickname() throws Exception {
        NicknameDto nicknameDto = new NicknameDto("뱅뱅뱅");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/nickname")
                .content(objectMapper.writeValueAsString(nicknameDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("닉네임 중복 여부 컨트롤러 오류 테스트 - 형식 오류")
    void errorNickname() throws Exception {
        NicknameDto nicknameDto = new NicknameDto("bang12");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/nickname")
                        .content(objectMapper.writeValueAsString(nicknameDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 컨트롤러 테스트")
    void login() throws Exception {
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("010-1234-5678").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .content(objectMapper.writeValueAsString(userLoginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 컨트롤러 오류 테스트- 형식 오류")
    void errorLogin() throws Exception {
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("0101234-5678").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .content(objectMapper.writeValueAsString(userLoginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 컨트롤러 오류 테스트- 존재하지 않는 아이디")
    void errorLogin2() throws Exception {
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("010-1234-5679").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .content(objectMapper.writeValueAsString(userLoginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}