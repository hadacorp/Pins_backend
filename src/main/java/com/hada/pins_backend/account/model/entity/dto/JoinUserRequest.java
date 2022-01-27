package com.hada.pins_backend.account.model.entity.dto;

import com.hada.pins_backend.advice.ValidationGroups.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 * Modified by parksuho on 2022/01/27.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserRequest {
    @NotBlank(message = "이름은 필수 값입니다.", groups = NotEmptyGroup.class)
    @Size(min = 1,max = 4,
            message = "이름은 최소 {min}자 이상, 최대 {max}자 이하 입니다.", groups = SizeCheckGroup.class)
    private String name;
    @NotBlank(message = "닉네임은 필수 값입니다.", groups = NotEmptyGroup.class)
    @Size(min = 2,max = 8,
            message = "닉네임은 최소 {min}자 이상, 최대 {max}자 이하 입니다.", groups = SizeCheckGroup.class)
    @Pattern(regexp = "^[가-힣|0-9]+$",
            message = "닉네임 형식이 아닙니다.", groups = PatternCheckGroup.class)
    private String nickName;
    @NotBlank(message = "주민번호는 필수 값입니다.", groups = NotEmptyGroup.class)
    @Pattern(regexp = "\\d{6}-[1-4]$",
            message = "주민번호 형식이 아닙니다.", groups = PatternCheckGroup.class)
    private String resRedNumber;
    @NotBlank(message = "핸드폰 번호는 필수 값입니다.", groups = NotEmptyGroup.class)
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "핸드폰 번호 형식이 아닙니다.", groups = PatternCheckGroup.class)
    private String phoneNum;
}
