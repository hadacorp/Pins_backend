package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.advice.ValidationGroups;
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
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @Size(min = 1,max = 4,
            message = "이름은 최소 {min}자 이상, 최대 {max}자 이하 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    private String name;
    @Size(min = 2,max = 8,
            message = "닉네임은 최소 {min}자 이상, 최대 {max}자 이하 입니다.", groups = ValidationGroups.SizeCheckGroup.class)
    @Pattern(regexp = "^[가-힣|0-9]+$",
            message = "닉네임 형식이 아닙니다.", groups = ValidationGroups.PatternCheckGroup.class)
    private String nickName;
}
