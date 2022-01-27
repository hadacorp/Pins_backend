package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.account.model.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/*
 * Created by parksuho on 2022/01/19.
 * Created by parksuho on 2022/01/27.
 */
@Getter
public class UserDto {
    private final Long id;
    private final String name;
    private final String phoneNum;
    private final String nickName;
    private final String profileImage;
    private final List<String> role;

    @Builder
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNum = user.getPhoneNum();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.role = user.getRoles();
    }
}
