package com.hada.pins_backend.domain.meetingPin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
@SpringBootTest
class MeetingPinRepositoryTest {

    @Autowired
    private MeetingPinRepository meetingPinRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void createUser(){
        User user = User.builder()
                .name("bang")
                .nickName("하나둘")
                .resRedNumber("980103-1")
                .phoneNum("010-7760-6393")
                .age(24)
                .gender(Gender.Male)
                .image("http;//...")
                .build();
        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void Test1(){

        MeetingPin meetingPin = MeetingPin.builder()
                .title("제목1")
                .createUser(userRepository.getById(1L))
                .content("내용1111")
                .category("운동")
                .setGender(Gender.Both)
                .setAge("10-20")
                .date(LocalDateTime.parse("2021-01-03 13:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .latitude(23.123434)
                .longitude(35.1234)
                .setLimit(5)
                .build();

        meetingPinRepository.save(meetingPin);

        meetingPinRepository.findAll().forEach(System.out::println);
    }
}