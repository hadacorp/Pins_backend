package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.UserAndMeetingPin;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

    private int resRedNumber;

    private String phoneNum;

    private int age;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String image;

    @OneToMany(mappedBy = "createUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MeetingPin> meetingPins;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserAndMeetingPin> userAndMeetingPins;

    @Builder
    public User(String name,
                String nickName,
                int resRedNumber,
                String phoneNum,
                int age,
                Gender gender,
                String image){
        this.name = name;
        this.nickName = nickName;
        this.resRedNumber = resRedNumber;
        this.phoneNum = phoneNum;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }








}
