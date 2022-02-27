package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.advice.ValidationGroups.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by parksuho on 2021/01/21.
 * Modified by parksuho on 2021/02/26.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckNickNameRequest {
    @NotBlank(message = "닉네임은 필수 값입니다.", groups = NotEmptyGroup.class)
    @Size(min = 2,max = 8,
            message = "닉네임은 최소 {min}자 이상, 최대 {max}자 이하 입니다.", groups = SizeCheckGroup.class)
    @Pattern(regexp = "^[가-힣|0-9]+$",
            message = "닉네임 형식이 아닙니다.", groups = PatternCheckGroup.class)
    private String nickName;
}
