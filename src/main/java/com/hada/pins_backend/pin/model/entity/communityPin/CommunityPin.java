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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/03/13.
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
    @Column(name = "latitude")
    private Double latitude;

    @NotNull
    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "TINYINT")
    private Gender gender;

    @NotNull
    @Column(name = "max_age")
    private Integer maxAge;

    @NotNull
    @Column(name = "min_age")
    private Integer minAge;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

    @ElementCollection
    @CollectionTable(name = "community_pin_image", joinColumns = @JoinColumn(name = "id"))
    private Set<String> images = new HashSet<>();

    @NotNull
    @Column(name = "set_limit")
    private Integer setLimit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", columnDefinition = "TINYINT")
    private CommunityPinCategory communityPinCategory;

    @OneToMany(mappedBy = "requestCommunityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunityPinRequest> communityPinRequests = new HashSet<>();

    @OneToMany(mappedBy = "communityPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunityPinParticipants> communityPinParticipants = new HashSet<>();

    public enum CommunityPinCategory {
        Apartment, College
    }
}