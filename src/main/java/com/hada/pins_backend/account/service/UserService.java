package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.model.entity.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 * Modified by parksuho on 2022/01/26.
 * Modified by parksuho on 2022/01/27.
 */
public interface UserService {
    JoinUserResponse join(MultipartFile file, JoinUserRequest request) throws IOException;
    TokenDto login(LoginUserRequest request);
    UserDto userInfo(Long userId);
    Boolean checkOldUser(LoginUserRequest request);
    Boolean checkNickName(CheckNickNameRequest request);
    UserDto updateUser(Long userId, MultipartFile file, UpdateUserRequest request) throws IOException;
}
