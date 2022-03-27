package com.hada.pins_backend.pin.model.request;


import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.advice.ValidationGroups.*;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/22.
 * Modified by bangjinhyuk on 2022/03/26.
 */
@Getter
@Setter
@ToString
public class HomePinRequest {

    @NotNull(message = "위도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double latitude;

    @NotNull(message = "경도는 필수값입니다.", groups = NotEmptyGroup.class)
    private double longitude;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> meetDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime startMeetTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime endMeetTime;
    @Min(value = 20, message = "최소 나이는 20살 입니다.", groups = SizeCheckGroup.class)
    private Integer minAge;
    @Max(value = 100, message = "최대 나이는 100살 입니다.", groups = SizeCheckGroup.class)
    private Integer maxAge;
    private String keyWord;
    private Gender meetGender;
    private List<MeetingPin.MeetingPinCategory> meetingPinCategory;
    private List<CommunityPin.CommunityPinCategory> communityPinCategory;
    private List<StoryPin.StoryPinCategory> storyPinCategory;
}
