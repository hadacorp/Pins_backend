package com.hada.pins_backend.service.user;

import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.dto.user.response.JoinUserResponse;
import com.hada.pins_backend.dto.user.response.LoginUserResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
public interface UserService {
    ResponseEntity<JoinUserResponse> insertUser(JoinUserRequest joinUserRequest);
    Boolean checkOldUser(UserLoginForm userLoginForm);
    Boolean checkNickname(String nickname);
    LoginUserResponse login(UserLoginForm userLoginForm);
}
