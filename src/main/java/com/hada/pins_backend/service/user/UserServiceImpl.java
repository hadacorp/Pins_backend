package com.hada.pins_backend.service.user;

import com.hada.pins_backend.config.JwtTokenProvider;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import com.hada.pins_backend.exception.user.NotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<JoinUserResponse> insertUser(JoinUserRequest joinUserRequest) {
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
        String genderKor = "여자";
        if (genderNum%2==1) {
            gender = Gender.Male;
            genderKor = "남자";
        }
        else gender = Gender.Female;


        log.info("insertUser age ={}, gender = {}",age,gender);

        Optional<User> oldUser = userRepository.findByPhoneNum(joinUserRequest.getPhoneNum());

        if (oldUser.isEmpty()) {
            User user = User.builder()
                    .name(joinUserRequest.getName())
                    .nickName(joinUserRequest.getNickName())
                    .resRedNumber(joinUserRequest.getResRedNumber())
                    .phoneNum(joinUserRequest.getPhoneNum())
                    .age(age)
                    .gender(gender)
                    .image(joinUserRequest.getImage())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(JoinUserResponse.builder()
                    .phoneNum(user.getPhoneNum())
                    .nickName(user.getNickName())
                    .image(user.getImage())
                    .data(age+"세 "+genderKor)
                    .build());
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Override
    @Transactional
    public Boolean checkOldUser(UserLoginForm userLoginForm) {

        Optional<User> user = userRepository.findByPhoneNum(userLoginForm.getUserPhonenum());
        return user.isPresent();
    }

    @Override
    public Boolean checkNickname(String nickName) {
        String pattern = "^[가-힣|0-9]+$";
        Optional<User> user = userRepository.findByNickName(nickName);
        return user.isPresent();
    }

    @Override
    public LoginUserResponse login(UserLoginForm userLoginForm) {
        User member = userRepository.findByPhoneNum(userLoginForm.getUserPhonenum())
                .orElseThrow(NotExistException::new);
        log.info("User Roles : {}", member.getRoles());
        String jwtToken = JwtTokenProvider.createToken(member, member.getRoles());
        String genderKor = "여자";
        if (member.getGender() == Gender.Male) genderKor = "남자";
        return LoginUserResponse.builder()
                .phoneNum(member.getPhoneNum())
                .nickName(member.getNickName())
                .image(member.getImage())
                .data(member.getAge()+"세 "+genderKor)
                .jwtToken(jwtToken)
                .build();
    }
}
