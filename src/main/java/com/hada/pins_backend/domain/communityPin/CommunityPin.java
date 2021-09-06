package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPost.CommunityPost;
import com.hada.pins_backend.domain.storyPin.StoryPinComment;
import com.hada.pins_backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@ToString
@NoArgsConstructor
@Entity
public class CommunityPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User superUser;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String category;

    @Enumerated(value = EnumType.STRING)
    private Gender setGender;

    @NotNull
    private int minAge;

    @NotNull
    private int maxAge;

    @DecimalMin(value = "1")
    private int setLimit;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    private String image;

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MembersAndCommunityPin> membersAndCommunityPins;

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MiddleMgmtAndCommunityPin> middleMgmtAndCommunityPins;

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CommunityPost> communityPosts;

    @Builder
    public CommunityPin(User createUser,
                        User superUser,
                        String title,
                        String content,
                        String category,
                        Gender setGender,
                        int minAge,
                        int maxAge,
                        int setLimit,
                        double longitude,
                        double latitude,
                        String image){
        this.createUser = createUser;
        this.superUser = superUser;
        this.title = title;
        this.content = content;
        this.category = category;
        this.setGender = setGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.setLimit = setLimit;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
    }

}
