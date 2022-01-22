package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.model.entity.RefreshToken;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.RefreshTokenRepository;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.config.jwt.JwtTokenProvider;
import com.hada.pins_backend.advice.exception.CAlreadyResigterUserException;
import com.hada.pins_backend.advice.exception.CUnregisteredUserException;
import com.hada.pins_backend.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;

    @Transactional
    public ApiResponse<JoinUserResponse> join(JoinUserRequest request) {
        // 가입된 유저인지 검증
        if (userRepository.findByPhoneNum(request.getPhoneNum()).isPresent()) throw new CAlreadyResigterUserException();
        //나이 계산
        StringTokenizer resRedTokens = new StringTokenizer(request.getResRedNumber(),"-");
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

        User user = User.builder()
                .name(request.getName()).nickName(request.getNickName())
                .resRedNumber(request.getResRedNumber()).phoneNum(request.getPhoneNum())
                .age(age).gender(gender)
                .profileImage("tmp").roles(Collections.singletonList("ROLE_USER")).build();

        userRepository.save(user);
        return new ApiResponse<>(JoinUserResponse.builder()
                .phoneNum(user.getPhoneNum())
                .nickName(user.getNickName())
                .profileImage("tmp")
                .ageAndGender(age+"세 "+genderKor)
                .build());
    }

    @Transactional
    public ApiResponse<TokenDto> login(LoginUserRequest request) {
        // 회원 정보 존재하는지 확인
        User user = userRepository.findByPhoneNum(request.getPhoneNum()).orElseThrow(CUnregisteredUserException::new);
        // 로그인 된 상태이면 기존 refresh token 삭제
        if (tokenRepository.findByUserId(user.getId()).isPresent()) {
            RefreshToken refreshToken = tokenRepository.findByUserId(user.getId()).orElseThrow();
            tokenRepository.delete(refreshToken);
        }
        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getPhoneNum(), user.getRoles());
        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        return new ApiResponse<>(tokenDto);
    }

    @Transactional(readOnly = true)
    public ApiResponse<UserDto> userInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(CUnregisteredUserException::new);
        return new ApiResponse<>(UserDto.builder().user(user).build());
    }

    @Transactional(readOnly = true)
    public ApiResponse<Boolean> checkOldUser(LoginUserRequest request) {
        return new ApiResponse<>(userRepository.findByPhoneNum(request.getPhoneNum()).isPresent());
    }

    @Transactional(readOnly = true)
    public ApiResponse<Boolean> checkNickName(CheckNickNameRequest request) {
        return new ApiResponse<>(userRepository.findByNickName(request.getNickName()).isPresent());
    }
}
