package com.hada.pins_backend.pin.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
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

    private MeetingPinStatus meetingPinStatus;
//
//    public MeetingPinResponse meetingPintoResponse(MeetingPin meetingPin, String googleKey){
//
//        ConvertPin2Response convertPin2Response = new ConvertPin2Response();
//        this.id = meetingPin.getId();
//        this.title = meetingPin.getTitle();
//        this.content = meetingPin.getContent();
//        this.category = meetingPin.getCategory();
//        this.setGender = Gender.convert2Korean(meetingPin.getSetGender());
//        this.ageRange = meetingPin.getMinAge() +"세 ~ "+meetingPin.getMaxAge()+"세";
//        this.setLimit = meetingPin.getSetLimit();
//        this.address = convertPin2Response.GpsToAddress(meetingPin.getLatitude(), meetingPin.getLongitude(),googleKey);
//        this.date = convertPin2Response.localDateTime2String(meetingPin.getDate());
//        this.createdAt = convertPin2Response.createdAt2String(meetingPin.getCreatedAt());
//        this.createUser = convertPin2Response.user2CreateDetail(meetingPin.getCreateUser());
//        this.participantNum = meetingPin.getUserAndMeetingPins().size();
//        this.participantDetailList = convertPin2Response.user2ParticipantDetail(meetingPin.getUserAndMeetingPins());
//        return this;
//    }

    public MeetingPinResponse setMeetingPinStatus(MeetingPinStatus meetingPinStatus) {
        this.meetingPinStatus = meetingPinStatus;
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
