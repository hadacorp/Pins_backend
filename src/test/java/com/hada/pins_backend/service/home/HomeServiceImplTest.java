package com.hada.pins_backend.service.home;

import antlr.collections.List;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@SpringBootTest
class HomeServiceImplTest {
    @Autowired
    private MeetingPinRepository meetingPinRepository;
    @Autowired
    private CommunityPinRepository communityPinRepository;
    @Autowired
    private StoryPinRepository storyPinRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HomeServiceImpl homeService;
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
    @DisplayName("홈화면 핀 로딩 기능")
    void Test1(){
        System.out.println(homeService.loadPin("010-7760-6393",37.282083,127.043850));
    }

    @Test
    @DisplayName("홈화면 핀 키워드 검색 기능")
    void Test2(){
        System.out.println(homeService.searchPin("010-7760-6393","아주대",37.282083,127.043850));
    }

    private void insertUser(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                "image1");
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
                .build();

        meetingPinRepository.saveAll(Lists.newArrayList(meetingPin1,meetingPin2,meetingPin3));
    }

    private void insertCommunityPin(User user){
        CommunityPin communityPin1 = CommunityPin.builder()
                .superUser(user)
                .title("아주대학교 ** 소학회 모임")
                .content("** 소학회 모임입니다.")
                .category("대학/상담")
                .setGender(Gender.Both)
                .minAge(20)
                .maxAge(40)
                .setLimit(10)
                .latitude(37.287281)
                .longitude(127.046374)
                .build();

        CommunityPin communityPin2 = CommunityPin.builder()
                .superUser(user)
                .title("아주대학교 ## 소학회 모임")
                .content("## 소학회 모임입니다.")
                .category("문화생활")
                .setGender(Gender.Female)
                .minAge(20)
                .maxAge(40)
                .setLimit(10)
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
                .category("분실/실종")
                .latitude(37.280019)
                .longitude(127.043544)
                .build();
        StoryPin storyPin2 = StoryPin.builder()
                .createUser(user)
                .title("사거리 교통사고")
                .content("사거리 교통사고 보신분")
                .category("사건/사고")
                .latitude(37.278130)
                .longitude(127.043497)
                .build();

        storyPinRepository.saveAll(Lists.newArrayList(storyPin1,storyPin2));
    }
}