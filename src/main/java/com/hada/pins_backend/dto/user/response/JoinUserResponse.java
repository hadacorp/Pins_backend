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
public class JoinUserResponse {
    private String phoneNum;
    private String nickName;
    private String data;
    private String image;

    @Builder
    public JoinUserResponse(String phoneNum, String nickName, String data, String image) {
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.data = data;
        this.image = image;
    }
}
