package com.hada.pins_backend.account.model.enumable;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
public enum Gender {
    Male, Female, Both;

    public static String convert2Korean(Gender gender) {
        if(gender == Gender.Male) return "남자";
        else if(gender == Gender.Female) return "여자";
        else return "성별 무관";
    }
}
