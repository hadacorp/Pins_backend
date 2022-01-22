package com.hada.pins_backend.pin.model.response;

import lombok.*;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
