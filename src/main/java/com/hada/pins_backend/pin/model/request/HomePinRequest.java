package com.hada.pins_backend.pin.model.request;


import com.hada.pins_backend.advice.ValidationGroups.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@Getter
@Setter
@ToString
public class HomePinRequest {

    @NotNull(message = "위도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double latitude;

    @NotNull(message = "경도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double longitude;

    private String meetingPinCategory;
    private String meetDate;
    private String meetTime;
    private String meetGender;
    private String meetAge;
    private String communityPinCategory;
    private String storyPinCategory;
}
