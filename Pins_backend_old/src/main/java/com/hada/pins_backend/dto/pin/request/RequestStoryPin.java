package com.hada.pins_backend.dto.pin.request;

import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2021/09/08.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
public class RequestStoryPin {
    @NotBlank
    private String title;

    @NotNull
    private String content;

    @NotBlank
    private String category;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    private MultipartFile image;

    public StoryPin toStoryPin(User user,String uploadImageURL){
        return StoryPin.builder()
                .createUser(user)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .image(uploadImageURL)
                .build();
    }
}
