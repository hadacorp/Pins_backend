package com.hada.pins_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import com.hada.pins_backend.service.user.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by bangjinhyuk on 2021/09/01.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    private MeetingPinRepository meetingPinRepository;
    @Autowired
    private CommunityPinRepository communityPinRepository;
    @Autowired
    private StoryPinRepository storyPinRepository;
    @Autowired
    private UserRepository userRepository;
    /**
     * 테스트 핀과 유저 넣기
     */
    @BeforeEach
    public void insertPin(){
        insertUser();
        User user1 = userRepository.findAll().get(0);
        insertMeetingPin(user1);
        insertCommunityPin(user1);
        insertStoryPin(user1);
    }

    @Test
    @Disabled
    @DisplayName("홈화면 핀 로딩 mvc 테스트 ")
    void loadPin() throws Exception{
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/home/pin")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .param("latitude", "37.282083")
                        .param("longitude", "127.043850")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Disabled
    @DisplayName("홈화면 키워드 핀 검색 mvc 테스트 ")
    void searchPin() throws Exception{
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/home/search/pin")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .param("keyword","아주대")
                        .param("latitude", "37.282083")
                        .param("longitude", "127.043850")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("홈화면 핀 로딩 + 필터 mvc 테스트 ")
    void loadPinFilter() throws Exception{
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/home/pin")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .param("latitude", "37.282083")
                        .param("longitude", "127.043850")
                        .param("meetingPinCategory", "산책/반려동물")
                        .param("meetDate", "0-1-2-3")
                        .param("meetTime", "1-23")
                        .param("meetGender", "Male")
                        .param("meetAge", "20-30")
                        .param("communityPinCategory", "all")
                        .param("storyPinCategory", "#분실/실종")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("홈화면 키워드 핀 검색 + 필터 mvc 테스트 ")
    void searchPinFilter() throws Exception{
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/home/search/pin")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .param("keyword","아주대")
                        .param("latitude", "37.282083")
                        .param("longitude", "127.043850")
                        .param("meetingPinCategory", "산책/반려동물")
                        .param("meetDate", "0-1-2-3")
                        .param("meetTime", "1-23")
                        .param("meetGender", "Male")
                        .param("meetAge", "20-30")
                        .param("communityPinCategory", "all")
                        .param("storyPinCategory", "#분실/실종")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("홈화면 키워드 장소 검색 mvc 테스트 ")
    void searchLocation() throws Exception{
        LoginUserResponse loginUserResponse = userService.login(UserLoginForm.builder().userphonenum("010-7760-6393").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/home/search/location")
                        .header("X-AUTH-TOKEN",loginUserResponse.getJwtToken())
                        .param("keyword","아주대")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }



    private void insertUser(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                null);
        userService.insertUser(joinUserRequest);
    }

    private void insertMeetingPin(User user){
        MeetingPin meetingPin1 = MeetingPin.builder()
                .createUser(user)
                .title("Kfc에서 보실분")
                .content("kfc에서 만납시다.")
                .minAge(25)
                .maxAge(39)
                .setLimit(2)
                .setGender(Gender.Both)
                .category("맛집탐방")
                .latitude(37.278132)
                .longitude(127.043496)
                .date(LocalDateTime.now().plusDays(2))
                .build();

        MeetingPin meetingPin2 = MeetingPin.builder()
                .createUser(user)
                .title("아주대 정문에서 보실분")
                .content("정문에서 만납시다.")
                .minAge(20)
                .maxAge(25)
                .setGender(Gender.Both)
                .setLimit(2)
                .category("산책/반려동물")
                .latitude(37.280019)
                .longitude(127.043544)
                .date(LocalDateTime.now().plusDays(3))
                .build();


        MeetingPin meetingPin3 = MeetingPin.builder()
                .createUser(user)
                .title("아주대 후문에서 보실분")
                .content("후문에서 만납시다.")
                .minAge(20)
                .maxAge(39)
                .setGender(Gender.Both)
                .setLimit(2)
                .category("산책/반려동물")
                .latitude(37.287281)
                .longitude(127.046374)
                .date(LocalDateTime.now().plusDays(4))
                .build();

        meetingPinRepository.saveAll(Lists.newArrayList(meetingPin1,meetingPin2,meetingPin3));
    }

    private void insertCommunityPin(User user){
        CommunityPin communityPin1 = CommunityPin.builder()
                .superUser(user)
                .createUser(user)
                .title("아주대학교 ** 소학회 모임")
                .content("** 소학회 모임입니다.")
                .category("대화/친목")
                .setGender(Gender.Both)
                .minAge(20)
                .maxAge(40)
                .detail("detaile입니다")
                .participationType(0)
                .profileType(0)
                .latitude(37.287281)
                .longitude(127.046374)
                .build();

        CommunityPin communityPin2 = CommunityPin.builder()
                .superUser(user)
                .createUser(user)
                .title("아주대학교 ## 소학회 모임")
                .content("## 소학회 모임입니다.")
                .category("스터디/독서")
                .setGender(Gender.Female)
                .minAge(20)
                .maxAge(40)
                .detail("detaile입니다")
                .participationType(0)
                .profileType(0)
                .latitude(37.287287)
                .longitude(127.046378)
                .build();

        communityPinRepository.saveAll(Lists.newArrayList(communityPin1,communityPin2));
    }

    private void insertStoryPin(User user){

        StoryPin storyPin1 = StoryPin.builder()
                .createUser(user)
                .title("에어팟 잃어버리신분")
                .content("삼거리 횡단보도에서 파란색 에어팟을 찾았아요")
                .category("#분실/실종")
                .latitude(37.280019)
                .longitude(127.043544)
                .image("123")
                .build();
        StoryPin storyPin2 = StoryPin.builder()
                .createUser(user)
                .title("사거리 교통사고")
                .content("사거리 교통사고 보신분")
                .category("#사건사고")
                .latitude(37.278130)
                .longitude(127.043497)
                .image("123")
                .build();

        storyPinRepository.saveAll(Lists.newArrayList(storyPin1,storyPin2));
    }
}