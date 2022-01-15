package com.hada.pins_backend.dto.pin.response;

import com.hada.pins_backend.domain.storyPin.StoryPin;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/09/27.
 */
@Getter
@ToString
public class StoryPinResponse {
    private Long id;

    private Long createUserId;

    private String title;

    private String content;

    private String category;

    private String image;

    private double longitude;

    private double latitude;
    public StoryPinResponse storyPintoResponse(StoryPin storyPin){
        this.id = storyPin.getId();
        this.createUserId = storyPin.getCreateUser().getId();
        this.title = storyPin.getTitle();
        this.content = storyPin.getContent();
        this.image = storyPin.getImage();
        this.category = storyPin.getCategory();
        this.longitude = storyPin.getLongitude();
        this.latitude = storyPin.getLatitude();
        return this;
    }
}
