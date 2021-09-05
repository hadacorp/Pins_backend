package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.domain.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void Test1(){
        User user = User.builder()
                .name("bang")
                .nickName("닉네임12")
                .resRedNumber("980103-1")
                .phoneNum("010-7760-6393")
                .age(21)
                .gender(Gender.Male)
                .profileImage("http;//...")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
        entityManager.clear();
        User user1 = userRepository.findAll().get(0);
        System.out.println(user1.getAuthorities());

    }
}