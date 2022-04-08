package com.hada.pins_backend.pin.model.entity.communityPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.BaseTimeEntity;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinParticipants;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/13.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/28.
 * Modified by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/09.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "community_pin")
public class CommunityPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createUser;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "latitude")
    private Double latitude;

    @NotNull
    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender", columnDefinition = "TINYINT")
    private Gender genderLimit;

    @NotNull
    @Column(name = "max_age")
    @Max(100)
    private Integer maxAge;

    @NotNull
    @Column(name = "min_age")
    @Min(20)
    private Integer minAge;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

//    @ElementCollection
//    @CollectionTable(name = "community_pin_image", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "set_limit")
    private Integer setLimit;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", columnDefinition = "TINYINT")
    private CommunityPinCategory category;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "community_pin_type", columnDefinition = "TINYINT")
    private CommunityPinType communityPinType;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "participation_method", columnDefinition = "TINYINT")
    private ParticipationMethod participationMethod;

    @OneToMany(mappedBy = "requestCommunityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPinRequest> communityPinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPinParticipants> communityPinParticipants = new ArrayList<>();

    public enum CommunityPinCategory {
        FRIENDSHIP,
        WALK,
        PET,
        FOOD,
        CULTURAL,
        GAME,
        EXERCISE,
        MOUNTAIN,
        STUDY,
        JOURNEY,
        SCHOOL,
        APARTMENT,
        ETC
    }

    public enum CommunityPinType {
        ANONYMOUS, PERSONAL
    }

    public enum ParticipationMethod {
        FREE, APPLICATION
    }

    @Builder
    public CommunityPin(User createUser, String title, Double latitude, Double longitude,
                        LocalDateTime startedAt, Gender genderLimit, Integer maxAge, Integer minAge,
                        String content, Integer setLimit,
                        CommunityPinCategory category, CommunityPinType communityPinType, ParticipationMethod participationMethod) {
        this.createUser = createUser;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startedAt = startedAt;
        this.genderLimit = genderLimit;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.content = content;
        this.setLimit = setLimit;
        this.category = category;
        this.communityPinType = communityPinType;
        this.participationMethod = participationMethod;
    }

    public void addRequest(CommunityPinRequest request) { this.communityPinRequests.add(request); }

    public void addParticipant(CommunityPinParticipants participants) { this.communityPinParticipants.add(participants); }

    public void updateImage(String image) {
        this.image = image;
    }
}
