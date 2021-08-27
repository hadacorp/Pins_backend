package com.hada.pins_backend.service;

import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
/**
 * Created by bangjinhyuk on 2021/08/12.
 */
public interface UserService {
    JoinUserResponse insertUser(JoinUserRequest joinUserRequest);
    Boolean checkOldUser(UserLoginForm userLoginForm);
    Boolean checkNickname(String nickname);
}
