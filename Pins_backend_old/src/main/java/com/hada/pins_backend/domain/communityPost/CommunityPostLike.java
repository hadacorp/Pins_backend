package com.hada.pins_backend.domain.communityPost;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2021/08/22.
 */
@Getter
@ToString
@NoArgsConstructor
@Entity
public class CommunityPostLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @Builder
    public CommunityPostLike(User user,
                             CommunityPost communityPost
    ){
        this.user = user;
        this.communityPost = communityPost;
    }
}
