package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@NoArgsConstructor
@Entity
public class StoryPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User createUser;

    private String title;

    private String content;

    private String category;

    private double longitude;

    private double latitude;

    @Builder
    public StoryPin(User createUser,
                    String title,
                    String content,
                    String category,
                    double longitude,
                    double latitude){
        this.createUser = createUser;
        this.title = title;
        this.content = content;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
