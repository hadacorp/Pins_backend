package com.hada.pins_backend.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;

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
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected UserService userService;

    private User user;
    private TokenDto tokenDto;

    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(User.builder()
                .name("홍길동")
                .nickName("엘든링")
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
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void joinTest() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinUserRequest.builder()
                .name("name").nickName("1234").resRedNumber("880123-1").phoneNum("010-4321-4321").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());

        //when
        ResultActions perform = mockMvc.perform(multipart("/users/join")
                .file(new MockMultipartFile("profileImage", LocalDateTime.now()+".png", "image/png", "test".getBytes()))
                .file(request).accept(MediaType.APPLICATION_JSON));
        //then
        perform.andDo(print())
                .andDo(document("users/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("profileImage").description("유저 이미지"),
                                partWithName("request").description("유저 정보")
                        ),
                        requestPartFields("request",
                                fieldWithPath("name").description("유저 이름"),
                                fieldWithPath("nickName").description("유저 닉네임"),
                                fieldWithPath("resRedNumber").description("유저 주민번호"),
                                fieldWithPath("phoneNum").description("유저 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.phoneNum").description("유저 전화번호"),
                                fieldWithPath("data.nickName").description("유저 닉네임"),
                                fieldWithPath("data.ageAndGender").description("유저 나이 및 성별")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
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
                                fieldWithPath("phoneNum").description("전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.grantType").description("grantType"),
                                fieldWithPath("data.accessToken").description("accessToken"),
                                fieldWithPath("data.refreshToken").description("refreshToken"),
                                fieldWithPath("data.accessTokenExpireDate").description("accessToken 만료시간")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
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
                        requestHeaders(headerWithName("Authorization").description("회원 Access Token")),
                        responseFields(
                                fieldWithPath("data.id").description("회원 고유번호"),
                                fieldWithPath("data.name").description("회원 이름"),
                                fieldWithPath("data.phoneNum").description("회원 전화번호"),
                                fieldWithPath("data.nickName").description("회원 닉네임"),
                                fieldWithPath("data.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.role").description("회원 role")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
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
                                fieldWithPath("phoneNum").description("전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data").description("가입 여부")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    void checkNickName () throws Exception {
        //given
        String object = objectMapper.writeValueAsString(CheckNickNameRequest.builder().nickName("고인물").build());

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
                                fieldWithPath("nickName").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("data").description("닉네임 중복 여부")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(UpdateUserRequest.builder()
                .name("name").nickName("1234").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());

        //when
        ResultActions perform = mockMvc.perform(multipart("/users/update")
                .file(new MockMultipartFile("profileImage", LocalDateTime.now()+".png", "image/png", "test".getBytes()))
                .file(request)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDto.getAccessToken()));

        //then
        perform
                .andDo(print())
                .andDo(document("users/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("회원 Access Token")),
                        requestParts(
                                partWithName("profileImage").description("유저 이미지"),
                                partWithName("request").description("유저 정보")
                        ),
                        requestPartFields("request",
                                fieldWithPath("name").description("유저 이름"),
                                fieldWithPath("nickName").description("유저 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("data.id").description("회원 고유번호"),
                                fieldWithPath("data.name").description("회원 이름"),
                                fieldWithPath("data.phoneNum").description("회원 전화번호"),
                                fieldWithPath("data.nickName").description("회원 닉네임"),
                                fieldWithPath("data.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.role").description("회원 role")
                        )
                ))
                .andExpect(status().isOk());
    }
}