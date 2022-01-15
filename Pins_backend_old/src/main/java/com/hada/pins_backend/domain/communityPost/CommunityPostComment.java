package com.hada.pins_backend.domain.communityPost;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@ToString
@NoArgsConstructor
@Entity
public class CommunityPostComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @NotBlank
    private String content;

    @Builder
    public CommunityPostComment(User user,
                                CommunityPost communityPost,
                                String content
    ){
        this.user = user;
        this.communityPost = communityPost;
        this.content = content;
    }
}
