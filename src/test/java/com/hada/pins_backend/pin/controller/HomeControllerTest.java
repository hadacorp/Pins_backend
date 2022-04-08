package com.hada.pins_backend.pin.controller;

import com.hada.pins_backend.ControllerTest;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.LoginUserRequest;
import com.hada.pins_backend.account.model.entity.dto.TokenDto;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.documentation.HomeDocumentation;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by bangjinhyuk on 2022/03/26.
 * Modified by parksuho on 2022/04/08.
 */
class HomeControllerTest extends ControllerTest {
    User user;
    User user2;
    TokenDto tokenDto;




    @BeforeEach
    public void setUp(TestInfo info) {
        user = userRepository.save(User.builder()
                .name("주동석")
                .nickName("주주주")
                .resRedNumber("970404-1")
                .phoneNum("010-0000-0001")
                .age(21)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());

        user2 = userRepository.save(User.builder()
                .name("강선호")
                .nickName("강강강")
                .resRedNumber("970402-1")
                .phoneNum("010-0000-0002")
                .age(25)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        tokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-0000-0001").build());

        if(info.getTags().contains("getLocations"))
            return;

        List<MeetingPin> meetingPins = new ArrayList<>();
        meetingPins.add(MeetingPin.builder()
                .content("1번 만남핀test")
                .createUser(user)
                .dateTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.NOON))
                .genderLimit(Gender.Both)
                .latitude(37.2910659)
                .longitude(127.0458188)
                .category(MeetingPin.MeetingPinCategory.STUDY)
                .maxAge(25)
                .minAge(20)
                .setLimit(10)
                .build());
        meetingPins.add(MeetingPin.builder()
                .content("2번 만남핀test")
                .createUser(user)
                .dateTime(LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(15,10,10)))
                .genderLimit(Gender.Both)
                .latitude(37.2910610)
                .longitude(127.0458192)
                .category(MeetingPin.MeetingPinCategory.STUDY)
                .maxAge(25)
                .minAge(20)
                .setLimit(10)
                .build());
        List<CommunityPin> communityPins = new ArrayList<>();
        communityPins.add(CommunityPin.builder()
                .latitude(37.2910620)
                .longitude(127.0458200)
                .title("푸르지오 모임")
                .content("푸르지오 월드마크 커뮤니티test")
                .maxAge(25)
                .minAge(20)
                .genderLimit(Gender.Both)
                .category(CommunityPin.CommunityPinCategory.APARTMENT)
                .createUser(user2)
                .setLimit(10)
                .communityPinType(CommunityPin.CommunityPinType.PERSONAL)
                .participationMethod(CommunityPin.ParticipationMethod.FREE)
                .startedAt(LocalDateTime.now())
                .build());
        communityPins.add(CommunityPin.builder()
                .latitude(37.2910622)
                .longitude(127.0458112)
                .title("서울대 모임")
                .content("서울대학교 커뮤니티test")
                .maxAge(25)
                .minAge(20)
                .genderLimit(Gender.Both)
                .category(CommunityPin.CommunityPinCategory.SCHOOL)
                .createUser(user2)
                .setLimit(10)
                .communityPinType(CommunityPin.CommunityPinType.ANONYMOUS)
                .participationMethod(CommunityPin.ParticipationMethod.APPLICATION)
                .startedAt(LocalDateTime.now())
                .build());
        List<StoryPin> storyPins = new ArrayList<>();
        storyPins.add(StoryPin.builder()
                .category(StoryPin.StoryPinCategory.LOST)
                .content("에어팟 잃어버렸습니다..test")
                .createUser(user)
                .latitude(37.2910610)
                .longitude(127.0458198)
                .build());

        meetingPinRepository.saveAll(meetingPins);
        communityPinRepository.saveAll(communityPins);
        storyPinRepository.saveAll(storyPins);
    }

    @Test
    @Tag("getPins")
    @Transactional
    public void getList() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("latitude", "37.291985900000014");
        params.add("longitude", "127.048468299999998");
        params.add("keyword", "test");
        params.add("maxAge", "99");
        params.add("minAge", "20");
        params.add("meetDate", LocalDate.now()+","+LocalDate.now().plusDays(1)+","+LocalDate.now().plusDays(2)+","+LocalDate.now().plusDays(3)+","+LocalDate.now().plusDays(4));
        params.add("startMeetTime", "07:00:00");
        params.add("endMeetTime", "23:00:00");
        params.add("meetGender", "Both");
        params.add("meetingPinCategory", "WALK,STUDY");
        params.add("storyPinCategory", null);
        params.add("communityPinCategory", "APARTMENT");
        //when
        ResultActions actions = mockMvc.perform(get("/v1/home/pin")
                .header("Authorization", "Bearer " + tokenDto.getAccessToken())
                .params(params)
        );
        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(HomeDocumentation.getPins())
                .andReturn();
    }
    @Test
    @Tag("getLocations")
    @Transactional
    public void getLocations() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword", "광교");

        //when
        ResultActions actions = mockMvc.perform(get("/v1/home/search/location")
                .header("Authorization", "Bearer " + tokenDto.getAccessToken())
                .params(params)
        );
        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(HomeDocumentation.getLocations())
                .andReturn();
    }


}