package com.hada.pins_backend.pin.model.entity;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.LoginUserRequest;
import com.hada.pins_backend.account.model.entity.dto.TokenDto;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.RefreshTokenRepository;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.account.service.UserService;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinRequest;
import com.hada.pins_backend.pin.model.enumable.State;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2022/03/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class MeetingPinTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingPinRepository meetingPinRepository;

    @Autowired
    MeetingPinRequestRepository meetingPinRequestRepository;

    @Autowired
    UserService userService;

    User user;
    User user2;
    TokenDto tokenDto;

    @PersistenceContext
    private EntityManager em;


    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        meetingPinRepository.deleteAllInBatch();

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

        user2 = userRepository.save(User.builder()
                .name("이승횬")
                .nickName("ㅣㅣㅏㅣㅏㅣ")
                .resRedNumber("970402-1")
                .phoneNum("010-1111-2222")
                .age(25)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        tokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-1234-1234").build());
    }

    @Test
    @Transactional
    @Disabled
    @DisplayName(value = "만남핀 저장")
    public void insertMeetingPin(){
        //given
        MeetingPin meetingPin = MeetingPin.builder()
                .content("1231123")
                .createUser(user)
                .dateTime(LocalDateTime.of(2022,3,15,15,30,0))
                .gender(Gender.Both)
                .latitude(37.2910659)
                .longitude(127.0458188)
                .category(MeetingPin.MeetingPinCategory.study)
                .maxAge(25)
                .minAge(20)
                .setLimit(10)
                .build();
        logger.info("==== meeting save ====");
        meetingPinRepository.save(meetingPin);
        logger.info("==== end meeting save ====");
        meetingPin.getImages().add("1번 이미지");
        meetingPin.getImages().add("2번 이미지");
        logger.info("==== flush ====");
        em.flush();
        em.clear();
        logger.info("==== end flush ====");
        meetingPin = meetingPinRepository.findById(1L).get();
        user2 = userRepository.findById(2L).get();
        MeetingPinRequest meetingPinRequest = MeetingPinRequest.builder()
                .requestUser(user2)
                .requestMeetingPin(meetingPin)
                .content("123")
                .state(State.Requested).build();
        logger.info("==== meetingPinRequest save  ====");
        user2.getMeetingPinRequests().add(meetingPinRequest);
//        meetingPinRequestRepository.save(meetingPinRequest);
        em.flush();
        em.clear();
        logger.info("==== end meetingPinRequest save  ====");
        //when
        //then
        meetingPin = meetingPinRepository.findById(1L).get();
        user2 = userRepository.findById(2L).get();
        assertEquals(meetingPin.getImages().size(),2);
        assertEquals(meetingPin.getMeetingPinRequests().size(),1);
        assertEquals(user2.getMeetingPinRequests().size(),1);
    }


}