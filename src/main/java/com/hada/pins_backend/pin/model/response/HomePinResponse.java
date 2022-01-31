package com.hada.pins_backend.pin.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@Getter
@NoArgsConstructor
@ToString
public class HomePinResponse implements Comparable<HomePinResponse>{

    private PinType pinType;
    private double distance;
    private Object pin;

    @Builder
    public HomePinResponse(PinType pinType, double distance, Object pin) {
        this.pinType = pinType;
        this.distance = distance;
        this.pin = pin;
    }

    @Override
    public int compareTo(HomePinResponse o) {
        return Double.compare(this.distance, o.distance);
    }

    public enum PinType {
        MeetingPin
    }
}
