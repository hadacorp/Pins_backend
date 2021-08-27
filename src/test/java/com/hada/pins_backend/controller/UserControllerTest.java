package com.hada.pins_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.config.JwtTokenProvider;
import com.hada.pins_backend.dto.user.NicknameDto;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.service.CustomUserDetailService;
import com.hada.pins_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
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
    void insertOne(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("아무개",
                "아아무무개",
                "011212-2",
                "010-1234-5678",
                "image2");
        JoinUserResponse joinUserResponse= userService.insertUser(joinUserRequest);
    }

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    void join() throws Exception{
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                "image1");
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
                "bang",
                "980103-1",
                "010-7760-6393",
                "image1");
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

}