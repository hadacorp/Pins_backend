package com.hada.pins_backend.service;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2021/08/08.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

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
}
