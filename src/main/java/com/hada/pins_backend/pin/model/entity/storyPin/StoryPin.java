package com.hada.pins_backend.pin.model.entity.storyPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/13.
 * Modified by bangjinhyuk on 2022/03/19.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "story_pin")
public class StoryPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createUser;

    @NotNull
    @Column(name = "latitude")
    private Double latitude;

    @NotNull
    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Column(name = "category", columnDefinition = "TINYINT")
    @Enumerated(value = EnumType.ORDINAL)
    private StoryPinCategory category;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

    @ElementCollection
    @CollectionTable(name = "story_pin_image", joinColumns = @JoinColumn(name = "id"))
    private Set<String> images = new HashSet<>();

    public enum StoryPinCategory {
        CURIOUS,
        REVIEW,
        TOWN,
        PET,
        HELP,
        ACCIDENT,
        LOST,
        CHAT,
        ETC
    }

    @Builder
    public StoryPin(User createUser, Double latitude, Double longitude, StoryPinCategory category, String content) {
        this.createUser = createUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.content = content;
    }
}
