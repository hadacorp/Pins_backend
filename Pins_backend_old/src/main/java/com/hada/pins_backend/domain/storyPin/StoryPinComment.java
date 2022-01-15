package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@NoArgsConstructor
@ToString
@Entity
public class StoryPinComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_pin_id")
    private StoryPin storyPin;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writeUser;

    @NotBlank
    private String content;

    @Builder
    public StoryPinComment(User writeUser,
                           StoryPin storyPin,
                           String content
                           ){
        this.writeUser = writeUser;
        this.storyPin = storyPin;
        this.content = content;
    }
}
