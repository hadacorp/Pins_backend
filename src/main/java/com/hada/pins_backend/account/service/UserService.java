package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.model.entity.dto.*;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 * Modified by parksuho on 2022/01/26.
 */
public interface UserService {
    JoinUserResponse join(JoinUserRequest request);
    TokenDto login(LoginUserRequest request);
    UserDto userInfo(Long userId);
    Boolean checkOldUser(LoginUserRequest request);
    Boolean checkNickName(CheckNickNameRequest request);
}
