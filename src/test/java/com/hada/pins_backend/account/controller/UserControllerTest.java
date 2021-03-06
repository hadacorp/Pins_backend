package com.hada.pins_backend.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.ControllerTest;
import com.hada.pins_backend.PinsBackendApplication;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.RefreshTokenRepository;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.account.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by parksuho on 2022/02/26.
 * Modified by parksuho on 2022/03/10.
 * Modified by parksuho on 2022/03/26.
 * Modified by parksuho on 2022/04/06.
 */
class UserControllerTest extends ControllerTest {
    private User user;
    private TokenDto tokenDto;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(User.builder()
                .name("?????????")
                .nickName("?????????")
                .resRedNumber("970404-1")
                .phoneNum("010-1234-1234")
                .age(21)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        tokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-1234-1234").build());
    }

    @AfterEach
    public void setDown() {
    }

    @Test
    @Transactional
    void joinTest() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinUserRequest.builder()
                .name("name").nickName("1234").resRedNumber("880123-1").phoneNum("010-4321-4321").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());

        //when
        ResultActions perform = mockMvc.perform(multipart("/users/join")
                .file(new MockMultipartFile("profileImage", UUID.randomUUID().toString() +".png", "image/png", "test".getBytes()))
                .file(request).accept(MediaType.APPLICATION_JSON));
        //then
        perform.andDo(print())
                .andDo(document("users/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("profileImage").description("?????? ?????????"),
                                partWithName("request").description("?????? ??????")
                        ),
                        requestPartFields("request",
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("nickName").description("?????? ?????????"),
                                fieldWithPath("resRedNumber").description("?????? ????????????"),
                                fieldWithPath("phoneNum").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("data.phoneNum").description("?????? ????????????"),
                                fieldWithPath("data.nickName").description("?????? ?????????"),
                                fieldWithPath("data.ageAndGender").description("?????? ?????? ??? ??????")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void login() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(LoginUserRequest.builder()
                .phoneNum("010-1234-1234").build());

        //when
        ResultActions actions = mockMvc.perform(post("/users/login")
                .content(object)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        actions
                .andDo(print())
                .andDo(document("users/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("phoneNum").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("data.grantType").description("grantType"),
                                fieldWithPath("data.accessToken").description("accessToken"),
                                fieldWithPath("data.refreshToken").description("refreshToken"),
                                fieldWithPath("data.accessTokenExpireDate").description("accessToken ????????????")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void findUserInfo() throws Exception {
        //given
        ResultActions actions = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + tokenDto.getAccessToken()));
        //then
        //when
        actions
                .andDo(print())
                .andDo(document("users/me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("?????? Access Token")),
                        responseFields(
                                fieldWithPath("data.id").description("?????? ????????????"),
                                fieldWithPath("data.name").description("?????? ??????"),
                                fieldWithPath("data.phoneNum").description("?????? ????????????"),
                                fieldWithPath("data.nickName").description("?????? ?????????"),
                                fieldWithPath("data.profileImage").description("?????? ????????? ?????????"),
                                fieldWithPath("data.role").description("?????? role")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void oldUser () throws Exception {
        //given
        String object = objectMapper.writeValueAsString(LoginUserRequest.builder()
                .phoneNum("010-1234-1234").build());

        //when
        ResultActions actions = mockMvc.perform(post("/users/old-user")
                .content(object)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        actions
                .andDo(print())
                .andDo(document("users/old-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("phoneNum").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("data").description("?????? ??????")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void checkNickName () throws Exception {
        //given
        String object = objectMapper.writeValueAsString(CheckNickNameRequest.builder().nickName("?????????").build());

        //when
        ResultActions actions = mockMvc.perform(post("/users/nickname")
                .content(object)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        actions
                .andDo(print())
                .andDo(document("users/nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickName").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("data").description("????????? ?????? ??????")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(UpdateUserRequest.builder()
                .name("name").nickName("1234").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());

        //when
        ResultActions perform = mockMvc.perform(multipart("/users/update")
                .file(new MockMultipartFile("profileImage", UUID.randomUUID().toString()+".png", "image/png", "test".getBytes()))
                .file(request)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDto.getAccessToken()));

        //then
        perform
                .andDo(print())
                .andDo(document("users/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("?????? Access Token")),
                        requestParts(
                                partWithName("profileImage").description("?????? ?????????"),
                                partWithName("request").description("?????? ??????")
                        ),
                        requestPartFields("request",
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("nickName").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("data.id").description("?????? ????????????"),
                                fieldWithPath("data.name").description("?????? ??????"),
                                fieldWithPath("data.phoneNum").description("?????? ????????????"),
                                fieldWithPath("data.nickName").description("?????? ?????????"),
                                fieldWithPath("data.profileImage").description("?????? ????????? ?????????"),
                                fieldWithPath("data.role").description("?????? role")
                        )
                ))
                .andExpect(status().isOk());
    }
}