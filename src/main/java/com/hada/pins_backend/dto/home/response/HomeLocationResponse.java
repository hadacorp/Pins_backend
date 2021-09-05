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
    private double x;
    private double y;

    @Builder
    public HomeLocationResponse(String placeName, double x, double y) {
        this.placeName = placeName;
        this.x = x;
        this.y = y;
    }
}
