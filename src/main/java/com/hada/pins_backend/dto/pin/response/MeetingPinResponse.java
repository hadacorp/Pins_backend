package com.hada.pins_backend.dto.pin.response;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.service.pin.ConvertPin2Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/09/27.
 */
@Getter
@ToString
public class MeetingPinResponse {
    private Long id;

    private CreateDetail createUser;

    private String title;

    private String content;

    private String category;

    private String setGender;

    private String ageRange;

    private int setLimit;

    private int participantNum;

    private String address;

    private String date;

    private String createdAt;

    private List<ParticipantDetail> participantDetailList = new ArrayList<>();

    public MeetingPinResponse meetingPintoResponse(MeetingPin meetingPin, String googleKey){

        this.id = meetingPin.getId();
        this.title = meetingPin.getTitle();
        this.content = meetingPin.getContent();
        this.category = meetingPin.getCategory();
        this.setGender = Gender.convert2Korean(meetingPin.getSetGender());
        this.ageRange = meetingPin.getMinAge() +"세 ~ "+meetingPin.getMaxAge()+"세";
        this.setLimit = meetingPin.getSetLimit();
        this.address = ConvertPin2Response.GpsToAddress(meetingPin.getLatitude(), meetingPin.getLongitude(),googleKey);
        this.date = ConvertPin2Response.localDateTime2String(meetingPin.getDate());
        this.createdAt = ConvertPin2Response.createdAt2String(meetingPin.getCreatedAt());
        this.createUser = ConvertPin2Response.user2CreateDetail(meetingPin.getCreateUser());
        this.participantNum = meetingPin.getUserAndMeetingPins().size();
        this.participantDetailList = ConvertPin2Response.user2ParticipantDetail(meetingPin.getUserAndMeetingPins());

        return this;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class ParticipantDetail {
        Long id;
        String nickName;
        String detail;
        String image;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateDetail {
        Long id;
        String nickName;
        String detail;
        String image;
    }

}
