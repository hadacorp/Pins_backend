package com.hada.pins_backend.domain;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
public enum Gender {
    Male, Female, Both;

    public static String convert2Korean(Gender gender) {
        if(gender == Gender.Male) return "남자";
        else if(gender == Gender.Female) return "여자";
        else return "성별 무관";
    }
}
