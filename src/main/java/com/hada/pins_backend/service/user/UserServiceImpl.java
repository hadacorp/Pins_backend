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
import com.hada.pins_backend.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private final S3Uploader s3Uploader;

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

        //이미지 넣어주는 부분 test:: 고정값 추가
        String uploadImageURL;

        try {
            if (joinUserRequest.getName().equals("방진혁")) uploadImageURL = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.27.png";
            else if (joinUserRequest.getName().equals("강선호")) uploadImageURL = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.46.png";
            else if (joinUserRequest.getName().equals("주동석")) uploadImageURL = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.15.png";
            else if (joinUserRequest.getName().equals("이범수")) uploadImageURL = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.37.png";
            else if (joinUserRequest.getName().equals("이승현")) uploadImageURL = "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.56.png";
            else if (joinUserRequest.getProfileImage()==null) uploadImageURL= "없음";
            else uploadImageURL = s3Uploader.upload(joinUserRequest.getProfileImage(), "images", joinUserRequest.getPhoneNum());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }


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
                    .profileImage(uploadImageURL)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(JoinUserResponse.builder()
                    .phoneNum(user.getPhoneNum())
                    .nickName(user.getNickName())
                    .profileImage(user.getProfileImage())
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
                .image(member.getProfileImage())
                .data(member.getAge()+"세 "+genderKor)
                .jwtToken(jwtToken)
                .build();
    }
}
