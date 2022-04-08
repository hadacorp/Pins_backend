package com.hada.pins_backend.pin.model.request;


import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.advice.ValidationGroups;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/04/06.
 * Modified by parksuho on 2022/04/09.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingPinRequest {
    @NotNull(message = "위도는 필수값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private Double latitude;
    @NotNull(message = "경도는 필수값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private Double longitude;

    private MeetingPin.MeetingPinCategory category;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime dateTime;
    private Gender genderLimit;
    @Max(value = 100, message = "최대 나이는 100살 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer maxAge;
    @Min(value = 20, message = "최소 나이는 20살 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer minAge;
    @NotNull(message = "모집 인원은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Max(value = 200, message = "모집 인원은 최대 200명까지입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer setLimit;

    @NotBlank(message = "내용은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Size(message = "내용은 최대 1000자까지입니다.", max = 1000, groups = ValidationGroups.SizeCheckGroup.class)
    private String content;
}
