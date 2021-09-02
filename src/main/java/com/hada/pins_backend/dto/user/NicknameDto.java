package com.hada.pins_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameDto {
    @Size(min = 2,max = 8)
    @NotBlank
    @Pattern(regexp = "^[가-힣|0-9]+$")
    private String nickName;
}
