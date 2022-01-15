package com.hada.pins_backend.dto.pin.request;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
public class RequestCreateCommunityPin {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String category;

    @NotBlank
    private String setGender;

    @NotBlank
    private String detail;

    @NotNull
    // 0 자유 참가 방식, 1 참가 신청 방식
    private int participationType;

    @NotNull
    // 0 개인 프로필 참가, 1 익명 프로필 참가
    private int profileType;

    @NotNull
    private int minAge;

    @NotNull
    private int maxAge;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    private MultipartFile image;

    public CommunityPin toCommunityPin(User createUser,String imageurl){
        Gender renamGender;
        if (this.setGender.equals("Both")) renamGender = Gender.Both;
        else if (this.setGender.equals("Male")) renamGender = Gender.Male;
        else renamGender = Gender.Female;
        return CommunityPin.builder()
                .createUser(createUser)
                .superUser(createUser)
                .title(this.title)
                .detail(this.detail)
                .participationType(this.participationType)
                .profileType(this.profileType)
                .content(this.content)
                .category(this.category)
                .setGender(renamGender)
                .minAge(this.minAge)
                .maxAge(this.maxAge)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .image(imageurl)
                .build();
    }
}