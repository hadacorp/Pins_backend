package com.hada.pins_backend.account.model.entity;


import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank
//    @Size(min = 1,max = 4)
    private String name;

//    @Size(min = 2,max = 8)
//    @NotBlank
//    @Pattern(regexp = "^[가-힣|0-9]+$")
    private String nickName;

//    @NotBlank
//    @Pattern(regexp = "\\d{6}-[1-4]$")
    private String resRedNumber;

//    @NotBlank
//    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String phoneNum;

    @DecimalMin(value="20")
    private int age;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @NotBlank
    private String profileImage;

    private String backgroundImage;
    private String introduce;
    private double score;
    private String job;
    private String hobby;
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String name,
                String nickName,
                String resRedNumber,
                String phoneNum,
                int age,
                Gender gender,
                String profileImage,
                List<String> roles){
        this.name = name;
        this.nickName = nickName;
        this.resRedNumber = resRedNumber;
        this.phoneNum = phoneNum;
        this.age = age;
        this.gender = gender;
        this.profileImage = profileImage;
        this.roles = roles;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
