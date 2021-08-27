package com.hada.pins_backend.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@Getter
@AllArgsConstructor
public class JoinUserRequest {
    private String name;

    private String nickName;

    private String resRedNumber;

    private String phoneNum;

    private String image;

}
