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

    public CommunityPin toCommunityPin(User createUser,String imageurl){
        Gender renamGender;
        System.out.println("Gender =========> "+this.setGender);
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
