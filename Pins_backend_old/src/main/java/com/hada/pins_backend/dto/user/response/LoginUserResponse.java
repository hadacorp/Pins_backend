package com.hada.pins_backend.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@NoArgsConstructor
@Getter
@ToString
public class LoginUserResponse {
    private String phoneNum;
    private String nickName;
    private String data;
    private String image;
    private String jwtToken;

    @Builder
    public LoginUserResponse(String phoneNum, String nickName, String data, String image,String jwtToken) {
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.data = data;
        this.image = image;
        this.jwtToken = jwtToken;
    }
}
