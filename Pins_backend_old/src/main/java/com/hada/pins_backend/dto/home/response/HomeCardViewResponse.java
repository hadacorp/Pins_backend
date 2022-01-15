package com.hada.pins_backend.dto.home.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/09/12.
 */
@Getter
@ToString
@NoArgsConstructor
public class HomeCardViewResponse implements Comparable<HomeCardViewResponse>{
    private double distance;
    private String pinType;
    private String category;
    private Long pinDBId;
    private double latitude;
    private double longitude;
    private String date;
    private String title;
    private String image;
    private int like;
    private int comment;

    @Builder
    public HomeCardViewResponse(double distance, String pinType, String category, Long pinDBId, double latitude, double longitude, String date, String title, String image, int like, int comment) {
        this.distance = distance;
        this.pinType = pinType;
        this.category = category;
        this.pinDBId = pinDBId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.title = title;
        this.image = image;
        this.like = like;
        this.comment = comment;
    }

    @Override
    public int compareTo(HomeCardViewResponse o) {
        return Double.compare(this.distance, o.distance);
    }
}
