package com.hada.pins_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginForm {
    private String userphonenum;
    private String password;
}
