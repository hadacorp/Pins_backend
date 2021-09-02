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
    private double distance;
    private String pinType;
    private String category;
    private Long pinDBId;
    private double latitude;
    private double longitude;
    private String date;
    private String title;
    private String image;


    @Builder
    public HomePinResponse(double distance, String pinType, String category, Long pinDBId, double latitude, double longitude, String date, String title, String image) {
        this.distance = distance;
        this.pinType = pinType;
        this.category = category;
        this.pinDBId = pinDBId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.title = title;
        this.image = image;
    }

    @Override
    public int compareTo(HomePinResponse o) {
        return Double.compare(this.distance, o.distance);
    }
}
