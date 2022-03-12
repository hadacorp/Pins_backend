package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.advice.ValidationGroups.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/02/26.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {
    @NotBlank(message = "핸드폰 번호는 필수 값입니다.", groups = NotEmptyGroup.class)
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "핸드폰 번호 형식이 아닙니다.", groups = PatternCheckGroup.class)
    private String phoneNum;
}
