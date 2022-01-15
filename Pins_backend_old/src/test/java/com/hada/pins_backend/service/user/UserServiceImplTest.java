
package com.hada.pins_backend.service.user;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.io.IOException;
import java.net.URL;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    void insertOne() throws IOException {
//
//        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());
//
//        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
//                "뱅뱅뱅",
//                "980103-1",
//                "010-7760-6393",
//                file);
//        userService.insertUser(joinUserRequest);
//    }

    @Test
    @DisplayName("회원가입 테스트")
    void Test1() throws IOException {


        JoinUserRequest joinUserRequest = new JoinUserRequest("아무개",
                "아아무무개개",
                "001212-2",
                "010-1234-5678",
                null );

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

    @Test
    @DisplayName("문자인증 테스트")
    void Test5(){
        System.out.println(userService.sms("010-7760-6393","Y"));
    }

}