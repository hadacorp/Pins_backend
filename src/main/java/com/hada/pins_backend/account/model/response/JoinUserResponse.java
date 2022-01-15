package com.hada.pins_backend.account.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
@NoArgsConstructor
@Getter
@ToString
public class JoinUserResponse {
    private String phoneNum;
    private String nickName;
    private String data;
    private String profileImage;

    @Builder
    public JoinUserResponse(String phoneNum, String nickName, String data, String profileImage) {
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.data = data;
        this.profileImage = profileImage;
    }
}
