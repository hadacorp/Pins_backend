package com.hada.pins_backend.domain.meetingPin;


import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/08/05.
 */
@SpringBootTest
class UserAndMeetingPinRepositoryTest {

    @Autowired
    private UserAndMeetingPinRepository userAndMeetingPinRepository;

    @Autowired
    private MeetingPinRepository meetingPinRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void Test1() {
        User user1 = userRepository.findAll().get(0);
        User user2 = userRepository.findAll().get(1);
        User user3 = userRepository.findAll().get(2);

        MeetingPin meetingPin = MeetingPin.builder()
                .title("제목1")
                .createUser(user1)
                .content("내용1111")
                .category("운동")
                .setGender(Gender.Both)
                .setAge("10-20")
                .date(LocalDateTime.parse("2021-01-03 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .latitude(23.123434)
                .longitude(35.1234)
                .setLimit(5)
                .build();
        meetingPinRepository.save(meetingPin);

//        meetingPinRepository.findAll().forEach(System.out::println);
        UserAndMeetingPin userAndMeetingPin1 = UserAndMeetingPin.builder()
                .member(user2)
                .meetingPin(meetingPin)
                .build();
        UserAndMeetingPin userAndMeetingPin2 = UserAndMeetingPin.builder()
                .member(user3)
                .meetingPin(meetingPin)
                .build();
        userAndMeetingPinRepository.saveAll(Lists.newArrayList(userAndMeetingPin1,userAndMeetingPin2));

//        entityManager.clear();
        entityManager.detach(meetingPin);
        System.out.println(userRepository.findAll().get(0).getMeetingPins());

        System.out.println(userRepository.findAll().get(1).getUserAndMeetingPins());

        System.out.println(meetingPinRepository.findAll().get(0).getUserAndMeetingPins());

        userAndMeetingPinRepository.findAll().forEach(System.out::println);
    }

    @Test
    void Test2(){
    }
}