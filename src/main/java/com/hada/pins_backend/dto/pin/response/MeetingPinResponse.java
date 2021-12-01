package com.hada.pins_backend.dto.pin.response;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.UserAndMeetingPin;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.service.pin.GpsToAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private List<ParticipantDetail> participantDetailList = new ArrayList<>();

    public MeetingPinResponse meetingPintoResponse(MeetingPin meetingPin){
        this.id = meetingPin.getId();

        StringBuilder userdetail = new StringBuilder();
        userdetail.append(meetingPin.getCreateUser().getAge()).append("세 ");
        if(meetingPin.getCreateUser().getGender() == Gender.Male) userdetail.append("남자");
        else userdetail.append("여자");

        this.createUser = new CreateDetail(
                meetingPin.getCreateUser().getId(),
                meetingPin.getCreateUser().getNickName(),
                userdetail.toString(),
                meetingPin.getCreateUser().getProfileImage()
                );


        this.title = meetingPin.getTitle();
        this.content = meetingPin.getContent();
        this.category = meetingPin.getCategory();


        if(meetingPin.getSetGender() == Gender.Male) this.setGender = "남자";
        else if(meetingPin.getSetGender() == Gender.Female) this.setGender = "여자";
        else this.setGender = "성별 무관";

        this.ageRange = meetingPin.getMinAge() +"세 ~ "+meetingPin.getMaxAge()+"세";

        this.setLimit = meetingPin.getSetLimit();

        GpsToAddress gps = null;
        try {
            gps = new GpsToAddress(meetingPin.getLatitude(), meetingPin.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps != null) {
            this.address = gps.getAddress();
        }


        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM월 dd일 HH시"));


        List<UserAndMeetingPin> userAndMeetingPins = meetingPin.getUserAndMeetingPins();

        this.participantNum = userAndMeetingPins.size();

        for(int i =0; i<userAndMeetingPins.size(); i++){
           User tmpUser = userAndMeetingPins.get(i).getMember();
           StringBuilder detail = new StringBuilder();
           detail.append(tmpUser.getAge()).append("세 ");
           if(tmpUser.getGender() == Gender.Male) detail.append("남자");
           else detail.append("여자");
           participantDetailList.add(
                   new ParticipantDetail(
                           tmpUser.getId(),
                           tmpUser.getNickName(),
                           detail.toString(),
                           tmpUser.getProfileImage()
                   )
           );
        }
        return this;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    private class ParticipantDetail {
        Long id;
        String nickName;
        String detail;
        String image;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    private class CreateDetail {
        Long id;
        String nickName;
        String detail;
        String image;
    }

}
