package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/10/06.
 */
@SpringBootTest
class AnonymousProfileRepositoryTest {

    @Autowired
    private AnonymousProfileRepository anonymousProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @Disabled
    void Test()throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.56.png").openStream());

        JoinUserRequest joinUserRequest1 = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                file);
        userService.insertUser(joinUserRequest1);

        AnonymousProfile anonymousProfile = AnonymousProfile.builder()
                .createUser(userRepository.findAll().get(0))
                .profileImage("123")
                .name("익명1").build();

        anonymousProfileRepository.save(anonymousProfile);
        System.out.println(anonymousProfileRepository.findAll().get(0));
    }
}