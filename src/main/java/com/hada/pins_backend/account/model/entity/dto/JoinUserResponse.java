package com.hada.pins_backend.account.model.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 */
@NoArgsConstructor
@Getter
@ToString
public class JoinUserResponse {
    private String phoneNum;
    private String nickName;
    private String ageAndGender;
//    private String profileImage;

    @Builder
    public JoinUserResponse(String phoneNum, String nickName, String ageAndGender, String profileImage) {
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.ageAndGender = ageAndGender;
//        this.profileImage = profileImage;
    }
}
