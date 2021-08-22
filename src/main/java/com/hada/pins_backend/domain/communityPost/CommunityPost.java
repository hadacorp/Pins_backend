package com.hada.pins_backend.domain.communityPost;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
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
public class CommunityPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createUser;

    @ManyToOne
    @JoinColumn(name = "community_pin_id")
    private CommunityPin communityPin;

    @NotBlank
    private String text;

    @NotNull
    private String image;

    @NotNull
    private String video;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;


    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CommunityPostLike> communityPostLikes;

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CommunityPostComment> communityPostComments;


    @Builder
    public CommunityPost(User createUser,
                         CommunityPin communityPin,
                         String text,
                         String image,
                         String video,
                         double latitude,
                         double longitude){
        this.createUser = createUser;
        this.communityPin = communityPin;
        this.text = text;
        this.image = image;
        this.video = video;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
