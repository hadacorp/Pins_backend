
package com.hada.pins_backend.service.user;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void insertOne(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                "image1");
        userService.insertUser(joinUserRequest);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void Test1(){
        JoinUserRequest joinUserRequest = new JoinUserRequest("아무개",
                "아아무무개개",
                "001212-2",
                "010-1234-5678",
                "image2");

        ResponseEntity<JoinUserResponse> responseEntity = userService.insertUser(joinUserRequest);
        System.out.println(responseEntity);
        User user = userRepository.findByPhoneNum("010-1234-5678")
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        System.out.println(user);
    }

    @Test
    @DisplayName("가입여부 확인 테스트")
    void Test2(){
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("010-7760-6393").build();
        System.out.println(userService.checkOldUser(userLoginForm));
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void Test3(){
        System.out.println(userService.checkNickname("bang"));
    }

    @Test
    @DisplayName("로그인 테스트")
    void Test4(){
        UserLoginForm userLoginForm = UserLoginForm.builder().userphonenum("010-7760-6393").build();
        System.out.println(userService.login(userLoginForm));
    }

}