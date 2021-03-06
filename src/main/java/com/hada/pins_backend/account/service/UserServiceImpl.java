package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.exception.CProfileImageInvalidException;
import com.hada.pins_backend.account.exception.CRefreshTokenNotFoundException;
import com.hada.pins_backend.account.exception.CUserNotFoundException;
import com.hada.pins_backend.account.model.entity.RefreshToken;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.RefreshTokenRepository;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.config.jwt.JwtTokenProvider;
import com.hada.pins_backend.account.exception.CAlreadyJoinUserException;
import com.hada.pins_backend.handler.FileHandler;
import com.hada.pins_backend.handler.S3Uploader;
import com.hada.pins_backend.model.FileFolderName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/19.
 * Modified by parksuho on 2022/01/26.
 * Modified by parksuho on 2022/01/27.
 * Modified by parksuho on 2022/01/30.
 * Modified by parksuho on 2022/01/31.
 * Modified by parksuho on 2022/02/07.
 * Modified by parksuho on 2022/02/27.
 * Modified by parksuho on 2022/04/05.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;
    private final FileHandler fileHandler;
    private final S3Uploader s3Uploader;

    private Map<String, Object> resRedNumberConvert(String resRedNumber) {
        Map<String, Object> details = new HashMap<String, Object>();
        StringTokenizer resRedTokens = new StringTokenizer(resRedNumber,"-");
        //?????? ??????
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String resRedToken = resRedTokens.nextToken();
        int yearOfBirth = Integer.parseInt(resRedToken.charAt(0) +String.valueOf(resRedToken.charAt(1)));
        if(yearOfBirth<year%100) details.put("age", year%100 - yearOfBirth);
        else details.put("age", 100-yearOfBirth + year%100 +1);
        //??????
        int genderNum = Integer.parseInt(resRedTokens.nextToken());
        if (genderNum % 2 == 1) {
            details.put("gender", Gender.Male);
            details.put("genderKor", "??????");
        } else {
            details.put("gender", Gender.Female);
            details.put("genderKor", "??????");
        }
        return details;
    }

    @Transactional
    public JoinUserResponse join(MultipartFile file, JoinUserRequest request) throws IOException {
        // ????????? ???????????? ??????
        if (userRepository.findByPhoneNum(request.getPhoneNum()).isPresent()) throw new CAlreadyJoinUserException();

        // ??????????????? ??????, ?????? ??????
        Map<String, Object> details = resRedNumberConvert(request.getResRedNumber());
        Integer age = (Integer) details.get("age");
        Gender gender = (Gender) details.get("gender");
        String genderKor = (String) details.get("genderKor");

        // ????????? ?????? ?????? ?????? ??????
        if (file.isEmpty())  {
            log.error("No image");
            throw new CProfileImageInvalidException();
        }

        // ????????? ???????????? ?????? path ??????
        String profileImagePath = fileHandler.parseFileInfo(file, request.getPhoneNum(), FileFolderName.profileImage);
        
        // ????????? S3??? ?????????
        String profileImage = s3Uploader.upload(file, profileImagePath);
        
        User user = User.builder()
                .name(request.getName()).nickName(request.getNickName())
                .resRedNumber(request.getResRedNumber()).phoneNum(request.getPhoneNum())
                .age(age).gender(gender)
                .profileImage(profileImage).role("ROLE_USER").build();
        userRepository.save(user);
        
        return JoinUserResponse.builder()
                .phoneNum(user.getPhoneNum())
                .nickName(user.getNickName())
                .profileImage(profileImagePath)
                .ageAndGender(age+"??? "+genderKor)
                .build();
    }

    @Transactional
    public TokenDto login(LoginUserRequest request) {
        // ?????? ?????? ??????????????? ??????
        User user = userRepository.findByPhoneNum(request.getPhoneNum()).orElseThrow(CUserNotFoundException::new);
        // ????????? ??? ???????????? ?????? refresh token ??????
        if (tokenRepository.findByUserId(user.getId()).isPresent()) {
            RefreshToken refreshToken = tokenRepository.findByUserId(user.getId()).orElseThrow(CRefreshTokenNotFoundException::new);
            tokenRepository.delete(refreshToken);
        }
        // AccessToken, RefreshToken ??????
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getPhoneNum(), user.getRole());
        // RefreshToken ??????
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional(readOnly = true)
    public UserDto userInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        return UserDto.builder().user(user).build();
    }

    @Transactional(readOnly = true)
    public Boolean checkOldUser(LoginUserRequest request) {
        return userRepository.findByPhoneNum(request.getPhoneNum()).isPresent();
    }

    @Transactional(readOnly = true)
    public Boolean checkNickName(CheckNickNameRequest request) {
        return userRepository.findByNickName(request.getNickName()).isPresent();
    }

    @Transactional
    public UserDto updateUser(Long userId, MultipartFile file, UpdateUserRequest request) throws IOException {
        User modifiedUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        String changedImage;
        if(file.isEmpty()) changedImage = modifiedUser.getProfileImage();
        else {
            changedImage = fileHandler.parseFileInfo(file, modifiedUser.getPhoneNum(), FileFolderName.profileImage);
            changedImage = s3Uploader.updateS3(file, modifiedUser.getProfileImage(), changedImage);
        }
        String changedName = (request.getName().isBlank()) ? modifiedUser.getName() : request.getName();
        String changedNickName = (request.getNickName().isBlank()) ? modifiedUser.getNickName() : request.getNickName();
        modifiedUser.update(changedImage, changedName, changedNickName);
        return UserDto.builder().user(modifiedUser).build();
    }
}
