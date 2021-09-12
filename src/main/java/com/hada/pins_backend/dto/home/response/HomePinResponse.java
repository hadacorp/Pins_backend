package com.hada.pins_backend.dto.home.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Getter
@NoArgsConstructor
@ToString
public class HomePinResponse{
    private String pinType;
    private String category;
    private Long pinDBId;
    private double latitude;
    private double longitude;

    @Builder
    public HomePinResponse(String pinType, String category, Long pinDBId, double latitude, double longitude) {
        this.pinType = pinType;
        this.category = category;
        this.pinDBId = pinDBId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
