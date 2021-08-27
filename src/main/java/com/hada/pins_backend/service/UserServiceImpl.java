package com.hada.pins_backend.service;

import com.hada.pins_backend.config.JwtTokenProvider;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

/**
 * Created by bangjinhyuk on 2021/08/08.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public JoinUserResponse insertUser(JoinUserRequest joinUserRequest) {
        //나이 계산
        StringTokenizer resRedTokens = new StringTokenizer(joinUserRequest.getResRedNumber(),"-");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String resRedToken = resRedTokens.nextToken();
        int yearOfBirth = Integer.parseInt(resRedToken.charAt(0) +String.valueOf(resRedToken.charAt(1)));
        int age;
        if(yearOfBirth<year%100) age = year%100 - yearOfBirth;
        else age = 100-yearOfBirth + year%100 +1;
        //성별
        int genderNum = Integer.parseInt(resRedTokens.nextToken());
        Gender gender;
        if (genderNum%2==1) gender = Gender.Male;
        else gender = Gender.Female;

        log.info("insertUser age ={}, gender = {}",age,gender);

        User user = User.builder()
                .name(joinUserRequest.getName())
                .nickName(joinUserRequest.getNickName())
                .resRedNumber(joinUserRequest.getResRedNumber())
                .phoneNum(joinUserRequest.getPhoneNum())
                .age(age)
                .gender(gender)
                .image(joinUserRequest.getImage())
                .roles(Collections.singletonList("USER"))
                .build();
        userRepository.save(user);

        return JoinUserResponse.builder()
                .phoneNum(user.getPhoneNum())
                .nickName(user.getNickName())
                .image(user.getImage())
                .data(age+"세 "+gender)
                .build();
    }

    @Override
    @Transactional
    public Boolean checkOldUser(UserLoginForm userLoginForm) {

        Optional<User> user = userRepository.findByPhoneNum(userLoginForm.getUserphonenum());
        return user.isPresent();
    }

    @Override
    public Boolean checkNickname(String nickname) {
        String pattern = "^[가-힣|0-9]+$";
        Optional<User> user = userRepository.findByNickName(nickname);
        return user.isPresent();
    }

    @Override
    public LoginUserResponse login(UserLoginForm userLoginForm) {
        User member = userRepository.findByPhoneNum(userLoginForm.getUserphonenum())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        log.info("User Roles : {}", member.getRoles());
        String jwtToken = JwtTokenProvider.createToken(member, member.getRoles());
        return LoginUserResponse.builder()
                .phoneNum(member.getPhoneNum())
                .nickName(member.getNickName())
                .image(member.getImage())
                .data(member.getAge()+"세 "+member.getGender())
                .jwtToken(jwtToken)
                .build();
    }
}
