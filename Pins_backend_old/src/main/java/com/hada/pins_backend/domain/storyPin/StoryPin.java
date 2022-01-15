package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@NoArgsConstructor
@ToString
@Entity
public class StoryPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createUser;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    @NotBlank
    private String category;

    private String image;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;


    @OneToMany(mappedBy = "storyPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<StoryPinLike> storyPinLikes;

    @OneToMany(mappedBy = "storyPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<StoryPinComment> storyPinComments;

    @Builder
    public StoryPin(User createUser,
                    String title,
                    String content,
                    String category,
                    String image,
                    double longitude,
                    double latitude){
        this.createUser = createUser;
        this.title = title;
        this.content = content;
        this.image = image;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
