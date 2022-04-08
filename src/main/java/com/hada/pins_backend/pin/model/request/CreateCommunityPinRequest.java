package com.hada.pins_backend.pin.model.request;

import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.advice.ValidationGroups;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/*
 * Created by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/08.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommunityPinRequest {
    @NotNull(message = "위도는 필수값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private Double latitude;
    @NotNull(message = "경도는 필수값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private Double longitude;

    private CommunityPin.CommunityPinCategory category;
    private CommunityPin.ParticipationMethod participationMethod;
    private CommunityPin.CommunityPinType communityPinType;
    private Gender genderLimit;
    @Max(value = 100, message = "최대 나이는 100살 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer maxAge;
    @Min(value = 20, message = "최소 나이는 20살 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer minAge;
    @NotNull(message = "모집 인원은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Max(value = 200, message = "모집 인원은 최대 200명까지입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private Integer setLimit;

    @NotBlank(message = "내용은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Size(message = "내용은 최대 30자까지입니다.", max = 30, groups = ValidationGroups.SizeCheckGroup.class)
    private String title;
    @NotBlank(message = "내용은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Size(message = "내용은 최대 1000자까지입니다.", max = 1000, groups = ValidationGroups.SizeCheckGroup.class)
    private String content;
}
