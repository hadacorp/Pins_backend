package com.hada.pins_backend.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by bangjinhyuk on 2021/08/27.
 */
@Getter
@AllArgsConstructor
public class JoinUserRequest {
    @NotBlank
    @Size(min = 1,max = 4)
    private String name;
    @Size(min = 2,max = 8)
    @NotBlank
    @Pattern(regexp = "^[가-힣|0-9]+$")
    private String nickName;
    @NotBlank
    @Pattern(regexp = "\\d{6}-[1-4]$")
    private String resRedNumber;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String phoneNum;
    @NotNull
    private MultipartFile image;

}
