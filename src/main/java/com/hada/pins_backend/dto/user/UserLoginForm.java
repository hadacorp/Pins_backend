package com.hada.pins_backend.dto.user;

import lombok.*;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@Getter
@NoArgsConstructor
public class UserLoginForm {
    private String userphonenum;

    @Builder
    public UserLoginForm(String userphonenum) {
        this.userphonenum = userphonenum;
    }
}
