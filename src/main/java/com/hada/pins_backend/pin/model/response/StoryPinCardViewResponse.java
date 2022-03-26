package com.hada.pins_backend.pin.model.response;


import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/19.
 */
@Getter
@ToString
public class StoryPinCardViewResponse {
    private Long id;

    private Long createUser;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createAt;

    private StoryPin.StoryPinCategory category;

    private String content;

    private Set<String> images = new HashSet<>();

    public static StoryPinCardViewResponse toStoryPinCardView(StoryPin storyPin){
        var response = new StoryPinCardViewResponse();
        response.id = storyPin.getId();
        response.createUser = storyPin.getCreateUser().getId();
        response.latitude = storyPin.getLatitude();
        response.longitude = storyPin.getLongitude();
        response.content = storyPin.getContent();
        response.images = storyPin.getImages();
        response.category = storyPin.getCategory();
        response.createAt = storyPin.getCreatedAt();
        return response;
    }

}
