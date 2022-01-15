package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2021/08/05.
 */
@Getter
@ToString
@NoArgsConstructor
@Entity
public class StoryPinLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "story_pin_id")
    private StoryPin storyPin;

    @Builder
    public StoryPinLike(User user,
                           StoryPin storyPin
    ){
        this.user = user;
        this.storyPin = storyPin;
    }

}
