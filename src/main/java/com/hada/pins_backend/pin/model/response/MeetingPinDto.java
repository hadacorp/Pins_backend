package com.hada.pins_backend.pin.model.response;

import com.hada.pins_backend.pin.model.entity.dto.SimpleUser;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/*
 * Created by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/08.
 */
@Getter
public class MeetingPinDto {
    private SimpleUser host;
    private MeetingPin.MeetingPinCategory category;

    private String content;
    private List<String> images;
    private String createdAt;

    private String location;
    private String meetingTime;
    private String ageLimit;
    private String genderLimit;

    private List<SimpleUser> participants;
    private Integer participantsNum;
    private Integer setLimit;

    @Builder
    public MeetingPinDto(SimpleUser host, MeetingPin.MeetingPinCategory category,
                         String content, List<String> images, String createdAt,
                         String location, String meetingTime, String ageLimit, String genderLimit,
                         List<SimpleUser> participants, Integer participantsNum, Integer setLimit) {
        this.host = host;
        this.category = category;

        this.content = content;
        this.images = images;
        this.createdAt = createdAt;

        this.location = location;
        this.meetingTime = meetingTime;
        this.ageLimit = ageLimit;
        this.genderLimit = genderLimit;

        this.participants = participants;
        this.participantsNum = participantsNum;
        this.setLimit = setLimit;
    }
}
