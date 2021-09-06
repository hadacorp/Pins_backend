package com.hada.pins_backend.dto.pin.request;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@Getter
@ToString
@NoArgsConstructor
public class RequestCreateCommunityPin {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String category;

    @NotBlank
    private String setGender;

    @NotNull
    private int minAge;

    @NotNull
    private int maxAge;

    @DecimalMin(value = "1")
    private int setLimit;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    private MultipartFile image;

    @Builder
    public RequestCreateCommunityPin(String title,
                                     String content,
                                     String category,
                                     String setGender,
                                     int minAge,
                                     int maxAge,
                                     int setLimit,
                                     double longitude,
                                     double latitude,
                                     MultipartFile image) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.setGender = setGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.setLimit = setLimit;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
    }

    public CommunityPin toCommunityPin(User createUser,String imageurl){
        Gender renamGender;
        if (this.setGender.equals("Both")) renamGender = Gender.Both;
        else if (this.setGender.equals("Male")) renamGender = Gender.Male;
        else renamGender = Gender.Female;
        return CommunityPin.builder()
                .createUser(createUser)
                .superUser(createUser)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .setGender(renamGender)
                .minAge(this.minAge)
                .maxAge(this.maxAge)
                .setLimit(this.setLimit)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .image(imageurl)
                .build();
    }
}
