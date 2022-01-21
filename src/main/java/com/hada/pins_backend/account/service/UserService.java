package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.model.entity.dto.*;
import com.hada.pins_backend.model.ApiResponse;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 */
public interface UserService {
    ApiResponse<JoinUserResponse> join(JoinUserRequest request);
    ApiResponse<TokenDto> login(LoginUserRequest request);
    ApiResponse<UserDto> userInfo(Long userId);
    ApiResponse<Boolean> checkOldUser(LoginUserRequest request);
    ApiResponse<Boolean> checkNickName(CheckNickNameRequest request);
}
