package com.hada.pins_backend.account.model.enumable;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/04/05.
 */
public enum Gender {
    Male, Female, Both;

    public static String convert2Korean(Gender gender) {
        if(gender == Gender.Male) return "남자만";
        else if(gender == Gender.Female) return "여자만";
        else return "성별 무관";
    }
}
