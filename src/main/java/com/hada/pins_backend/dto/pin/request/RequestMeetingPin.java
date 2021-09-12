package com.hada.pins_backend.dto.pin.request;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by bangjinhyuk on 2021/09/08.
 */
@Getter
@AllArgsConstructor
@ToString
@Builder
public class RequestMeetingPin {

    @NotBlank
    private String title;

    @NotNull
    private String content;

    @NotBlank
    private String category;

    @NotBlank
    private String setGender;

    @NotNull
    private int minAge;

    @NotNull
    private int maxAge;

    @DecimalMin(value = "1")
    private int setLimit;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    @NotNull
    private Long date;

    @NotNull
    private int hour;

    @NotNull
    private int minute;

    public MeetingPin toMeetingPin(User user, LocalDateTime renameDate) {
        Gender renamGender;
        if (this.setGender.equals("Both")) renamGender = Gender.Both;
        else if (this.setGender.equals("Male")) renamGender = Gender.Male;
        else renamGender = Gender.Female;
        return MeetingPin.builder()
                .createUser(user)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .setGender(renamGender)
                .minAge(this.minAge)
                .maxAge(this.maxAge)
                .setLimit(this.setLimit)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .date(renameDate)
                .build();
    }
}
