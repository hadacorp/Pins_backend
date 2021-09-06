package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.MembersAndCommunityPin;
import com.hada.pins_backend.domain.communityPin.MiddleMgmtAndCommunityPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.UserAndMeetingPin;
import com.hada.pins_backend.domain.storyPin.StoryPinComment;
import com.hada.pins_backend.domain.storyPin.StoryPinLike;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotBlank
    @Size(min = 1,max = 4)
    private String name;

    @Size(min = 2,max = 8)
    @NotBlank
    @Pattern(regexp = "^[가-힣|0-9]+$")
    private String nickName;

    @NotBlank
    @Pattern(regexp = "\\d{6}-[1-4]$")
    private String resRedNumber;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
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

    @OneToMany(mappedBy = "superUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CommunityPin> communityPins;

    @OneToMany(mappedBy = "createUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CommunityPin> communityPinList;

    @OneToMany(mappedBy = "middleManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MiddleMgmtAndCommunityPin> middleMgmtAndCommunityPins;

    @OneToMany(mappedBy = "communityMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MembersAndCommunityPin> membersAndCommunityPins;



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
        return "";
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
