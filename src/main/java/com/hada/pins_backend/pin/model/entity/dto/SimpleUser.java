package com.hada.pins_backend.pin.model.entity.dto;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import lombok.Getter;


/**
 * Created by bangjinhyuk on 2022/03/27.
 */
@Getter
public class SimpleUser {
    private Long id;
    private String nickName;
    private int age;
    private Gender gender;
    private String profileImage;

    public static SimpleUser of(User user){
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.id = user.getId();
        simpleUser.nickName = user.getNickName();
        simpleUser.age = user.getAge();
        simpleUser.gender = user.getGender();
        simpleUser.profileImage = user.getProfileImage();
        return simpleUser;
    }

}
