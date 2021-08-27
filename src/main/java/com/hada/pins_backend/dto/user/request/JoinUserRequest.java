package com.hada.pins_backend.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@Getter
@AllArgsConstructor
public class JoinUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String nickName;
    @NotBlank
    private String resRedNumber;
    @NotBlank
    private String phoneNum;
    @NotBlank
    private String image;

}
