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
public class HomePinResponse implements Comparable<HomePinResponse>{
    private String pinType;
    private String category;
    private Long pinDBId;
    private double latitude;
    private double longitude;
    private double distance;

    @Builder
    public HomePinResponse(String pinType, String category, Long pinDBId, double latitude, double longitude, double distance) {
        this.pinType = pinType;
        this.category = category;
        this.pinDBId = pinDBId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    @Override
    public int compareTo(HomePinResponse o) {
        return Double.compare(this.distance, o.distance);
    }
}
