package com.hada.pins_backend.account.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/*
 * Created by parksuho on 2022/01/19.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String phoneNum;
}
