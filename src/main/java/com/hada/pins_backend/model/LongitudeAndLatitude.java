package com.hada.pins_backend.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@Getter
@ToString
public class LongitudeAndLatitude {
    private static final double latitudeRange =  0.025;
    private static final double longitudeRange =  0.025;
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

