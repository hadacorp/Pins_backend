package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.UserAndMeetingPin;
import com.hada.pins_backend.domain.storyPin.StoryPinComment;
import com.hada.pins_backend.domain.storyPin.StoryPinLike;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<StoryPinLike> storyPinLikes;

    @OneToMany(mappedBy = "writeUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<StoryPinComment> storyPinComments;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phoneNum;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public User(String name,
                String nickName,
                int resRedNumber,
                String phoneNum,
                int age,
                Gender gender,
                String image,
                String password,
                List<String> roles){
        this.name = name;
        this.nickName = nickName;
        this.resRedNumber = resRedNumber;
        this.phoneNum = phoneNum;
        this.age = age;
        this.gender = gender;
        this.image = image;
        this.image = image;
        this.password = password;
        this.roles = roles;
    }








}
