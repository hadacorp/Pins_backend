package com.hada.pins_backend.dto.pin.response;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.user.User;
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

/**
 * Created by bangjinhyuk on 2021/09/27.
 */
@Getter
@ToString
public class MeetingPinResponse {
    private Long id;

    private Long createUserId;

    private String title;

    private String content;

    private String category;

    private Gender setGender;

    private int minAge;

    private int maxAge;

    private int setLimit;

    private double longitude;

    private double latitude;

    private LocalDateTime date;

    public MeetingPinResponse meetingPintoResponse(MeetingPin meetingPin){
        this.id = meetingPin.getId();
        this.createUserId = meetingPin.getCreateUser().getId();
        this.title = meetingPin.getTitle();
        this.content = meetingPin.getContent();
        this.category = meetingPin.getCategory();
        this.setGender = meetingPin.getSetGender();
        this.minAge = meetingPin.getMinAge();
        this.maxAge = meetingPin.getMaxAge();
        this.setLimit = meetingPin.getSetLimit();
        this.longitude = meetingPin.getLongitude();
        this.latitude = meetingPin.getLatitude();
        this.date = meetingPin.getDate();
        return this;
    }
}
