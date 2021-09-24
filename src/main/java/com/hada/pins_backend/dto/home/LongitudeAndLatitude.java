package com.hada.pins_backend.dto.home;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2021/09/24.
 */
@Getter
@ToString
public class LongitudeAndLatitude {
    private static double latitudeRange =  0.025;
    private static double longitudeRange =  0.025;
    private double latitude;
    private double longitude;

    public LongitudeAndLatitude(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getMaxLatitude(){
        return latitude + latitudeRange;
    }

    public double getMinLatitude(){
        return latitude - latitudeRange;
    }

    public double getMaxLongitude(){
        return longitude + longitudeRange;
    }

    public double getMinLongitude(){
        return longitude - longitudeRange;
    }

}
