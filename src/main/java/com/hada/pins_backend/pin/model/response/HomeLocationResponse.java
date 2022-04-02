package com.hada.pins_backend.pin.model.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@ToString
@Getter
public class HomeLocationResponse {
    List<LocationResponse> locationResponses;

    @JsonSetter("documents")
    public void setLocationResponses(List<LocationResponse> locationResponses) {
        this.locationResponses = locationResponses;
    }

    @Getter
    @ToString
    public static class LocationResponse {
        private String placeName;
        private double latitude;
        private double longitude;

        @JsonGetter("placeName")
        public String getPlaceName() {
            return placeName;
        }

        @JsonGetter("latitude")
        public double getLatitude() {
            return latitude;
        }

        @JsonGetter("longitude")
        public double getLongitude() {
            return longitude;
        }

        @JsonSetter("place_name")
        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        @JsonSetter("x")
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @JsonSetter("y")
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }


}
