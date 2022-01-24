package com.hada.pins_backend.pin.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@Getter
@AllArgsConstructor
@ToString
public class HomePinRequest {

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    private String meetingPinCategory;
    private String meetDate;
    private String meetTime;
    private String meetGender;
    private String meetAge;
    private String communityPinCategory;
    private String storyPinCategory;
}
