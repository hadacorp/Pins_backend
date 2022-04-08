package com.hada.pins_backend.account.model.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.BaseTimeEntity;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinParticipants;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinRequest;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinParticipants;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinRequest;
import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 * Modified by parksuho in 2022/01/27.
 * Modified by parksuho on 2022/02/27.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by parksuho on 2022/04/08.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String resRedNumber;

    @Column(nullable = false)
    private String phoneNum;

    @DecimalMin(value="20")
    @Column(nullable = false)
    private int age;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String profileImage;

    private String backgroundImage;
    private String introduce;
    private double score;
    private String job;
    private String hobby;
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "createUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingPin> meetingPins = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingPinParticipants> meetingPinParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "requestUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingPinRequest> meetingPinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "requestCommunityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPinRequest> communityPinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPinParticipants> communityPinParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "createUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPin> communityPins = new ArrayList<>();

    @OneToMany(mappedBy = "createUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryPin> storyPins = new ArrayList<>();


    @Builder
    public User(String name,
                String nickName,
                String resRedNumber,
                String phoneNum,
                int age,
                Gender gender,
                String profileImage,
                String role){
        this.name = name;
        this.nickName = nickName;
        this.resRedNumber = resRedNumber;
        this.phoneNum = phoneNum;
        this.age = age;
        this.gender = gender;
        this.profileImage = profileImage;
        this.role = role;
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

    public void update(String profileImage, String name, String nickName) {
        this.profileImage = profileImage;
        this.name = name;
        this.nickName = nickName;
    }
}
