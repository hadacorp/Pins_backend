package com.hada.pins_backend.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@Getter
@NoArgsConstructor
@ToString
public class UserLoginForm {
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String userPhonenum;

    @Builder
    public UserLoginForm(String userphonenum) {
        this.userPhonenum = userphonenum;
    }
}
