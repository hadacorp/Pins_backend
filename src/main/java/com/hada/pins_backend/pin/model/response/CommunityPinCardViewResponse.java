package com.hada.pins_backend.pin.model.response;


import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/19.
 */
@Getter
@ToString
public class CommunityPinCardViewResponse {
    private Long id;

    private Long createUser;

    private Double latitude;

    private Double longitude;

    private LocalDateTime startedAt;

    private String content;

    private Set<String> images = new HashSet<>();

    private Integer participantNum;

    private CommunityPin.CommunityPinCategory category;

    public static CommunityPinCardViewResponse toCommunityPinCardView(CommunityPin communityPin){
        var response = new CommunityPinCardViewResponse();
        response.id = communityPin.getId();
        response.createUser = communityPin.getCreateUser().getId();
        response.latitude = communityPin.getLatitude();
        response.longitude = communityPin.getLongitude();
        response.startedAt = communityPin.getStartedAt();
        response.content = communityPin.getContent();
        response.images = communityPin.getImages();
        response.participantNum = communityPin.getCommunityPinParticipants().size();
        response.category = communityPin.getCategory();
        return response;
    }

}
