package com.hada.pins_backend.pin.model.request;


import com.hada.pins_backend.advice.ValidationGroups.NotEmptyGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2022/03/22.
 */
@Getter
@Setter
@ToString
public class HomeLocationRequest {

    @NotNull(message = "위도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double latitude;

    @NotNull(message = "경도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double longitude;
}
