package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.account.model.entity.User;
import lombok.Getter;

import java.util.List;

/*
 * Created by parksuho on 2022/01/19.
 */
@Getter
public class UserDto {
    private final Long id;
    private final String name;
    private final String phoneNum;
    private final String nickName;
    private final List<String> role;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNum = user.getPhoneNum();
        this.nickName = user.getNickName();
        this.role = user.getRoles();
    }
}
