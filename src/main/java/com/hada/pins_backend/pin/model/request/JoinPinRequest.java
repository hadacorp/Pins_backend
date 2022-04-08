package com.hada.pins_backend.pin.model.request;

import com.hada.pins_backend.advice.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/*
 * Created by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/09.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinPinRequest {
    @NotBlank(message = "첫 인사는 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Size(message = "첫 인사는 최대 50자까지입니다.", max = 50, groups = ValidationGroups.SizeCheckGroup.class)
    private String content;
}
