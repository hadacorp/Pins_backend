package com.hada.pins_backend.pin.model.response;

import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinParticipants;
import com.hada.pins_backend.pin.model.entity.dto.SimpleUser;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/*
 * Created by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/08.
 */
@Getter
public class CommunityPinDto {
    private String title;
    private CommunityPin.CommunityPinType communityPinType;
    private CommunityPin.CommunityPinCategory category;

    private String content;
    private String image;
    private Integer participantsNum;

    private String location;
    private String startedAt;
    private String ageLimit;
    private String genderLimit;

    private Integer setLimit;
        private List<SimpleUser> participants;

    @Builder
    public CommunityPinDto(String title, CommunityPin.CommunityPinType communityPinType, CommunityPin.CommunityPinCategory category,
                           String content, String image, Integer participantsNum,
                           String location, String startedAt, String ageLimit, String genderLimit,
                           Integer setLimit, List<SimpleUser> participants) {
        this.title = title;
        this.communityPinType = communityPinType;
        this.category = category;

        this.content = content;
        this.image = image;
        this.participantsNum = participantsNum;

        this.location = location;
        this.startedAt = startedAt;
        this.ageLimit = ageLimit;
        this.genderLimit = genderLimit;

        this.setLimit = setLimit;
        this.participants = participants;
    }
}
