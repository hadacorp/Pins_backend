package com.hada.pins_backend.service.home;

import antlr.collections.List;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HomeServiceImpl homeService;
    @BeforeEach
    public void insertMeetingPin(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                "image1");
        userService.insertUser(joinUserRequest);

        MeetingPin meetingPin1 = MeetingPin.builder()
                .createUser(userRepository.findAll().get(0))
                .title("Kfc에서 보실분")
                .content("kfc에서 만납시다.")
                .setAge("20-39")
                .setLimit(2)
                .setGender(Gender.Both)
                .category("맛집탐방")
                .latitude(37.278132)
                .longitude(127.043496)
                .build();

        MeetingPin meetingPin2 = MeetingPin.builder()
                .createUser(userRepository.findAll().get(0))
                .title("아주대 정문에서 보실분")
                .content("정문에서 만납시다.")
                .setAge("20-39")
                .setGender(Gender.Both)
                .setLimit(2)
                .category("산책/반려동물")
                .latitude(37.280019)
                .longitude(127.043544)
                .build();


        MeetingPin meetingPin3 = MeetingPin.builder()
                .createUser(userRepository.findAll().get(0))
                .title("아주대 후문에서 보실분")
                .content("후문에서 만납시다.")
                .setAge("20-39")
                .setGender(Gender.Both)
                .setLimit(2)
                .category("산책/반려동물")
                .latitude(37.287281)
                .longitude(127.046374)
                .build();

        meetingPinRepository.saveAll(Lists.newArrayList(meetingPin1,meetingPin2,meetingPin3));

    }

    @Test
    void Test1(){
        homeService.loadPin("1234",37.282083,127.043850);
    }
}