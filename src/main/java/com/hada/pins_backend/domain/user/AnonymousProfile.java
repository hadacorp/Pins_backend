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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
public class AnonymousProfile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User createUser;

    @NotBlank
    @Size(min = 1,max = 4)
    private String name;

    @NotBlank
    private String profileImage;

    @Builder
    public AnonymousProfile(Long id, User createUser, String name, String profileImage) {
        this.id = id;
        this.createUser = createUser;
        this.name = name;
        this.profileImage = profileImage;
    }
}
