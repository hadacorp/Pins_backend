package com.hada.pins_backend.dto.home.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Getter
@NoArgsConstructor
public class HomePinResponse {
    private Long pinId;
    private String pinType;
    private Long pinDBId;
    private double latitude;
    private double longitude;

    @Builder
    public HomePinResponse(String pinType, Long pinId, Long pinDBId, double latitude, double longitude) {
        this.pinType = pinType;
        this.pinId = pinId;
        this.pinDBId = pinDBId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
