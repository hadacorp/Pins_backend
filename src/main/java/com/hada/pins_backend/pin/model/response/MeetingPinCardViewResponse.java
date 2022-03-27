package com.hada.pins_backend.pin.model.response;


import com.hada.pins_backend.pin.model.entity.dto.SimpleUser;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.service.GpsToAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/27.
 */
@Getter
@ToString
public class MeetingPinCardViewResponse {

    private Long id;

    private SimpleUser createUser;

    private Double latitude;

    private Double longitude;

    @Setter
    private String address;

    @Setter
    private String dateTime;

    private String content;

    private Set<String> images;

    private Integer setLimit;

    private Integer participantNum;

    private MeetingPin.MeetingPinCategory category;

    public static MeetingPinCardViewResponse toMeetingPinCardView(MeetingPin meetingPin,GpsToAddress gpsToAddress) {
        var response = new MeetingPinCardViewResponse();
        response.id = meetingPin.getId();
        response.createUser = SimpleUser.of(meetingPin.getCreateUser());
        response.latitude = meetingPin.getLatitude();
        response.longitude = meetingPin.getLongitude();
        response.content = meetingPin.getContent();
        response.address  = gpsToAddress.getAddress(meetingPin.getLatitude(),meetingPin.getLongitude());
        response.images = meetingPin.getImages();
        response.setLimit = meetingPin.getSetLimit();
        response.participantNum = meetingPin.getMeetingPinParticipants().size();
        response.category = meetingPin.getCategory();
        return response;
    }

}
