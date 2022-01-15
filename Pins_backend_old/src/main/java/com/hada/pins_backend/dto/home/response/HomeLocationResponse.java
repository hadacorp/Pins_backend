package com.hada.pins_backend.dto.home.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/09/05.
 */
@Getter
@ToString
@NoArgsConstructor
public class HomeLocationResponse {
    private String placeName;
    private double latitude;
    private double longitude;

    @Builder
    public HomeLocationResponse(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
