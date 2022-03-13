package com.hada.pins_backend.pin.model.entity.meetingPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "meeting_pin")
public class MeetingPin extends BaseTimeEntity {
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
    @Column(name = "date_time")
    private LocalDateTime dateTime;

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
    @CollectionTable(name = "meeting_pin_image", joinColumns = @JoinColumn(name = "id"))
    private Set<String> images = new HashSet<>();

    @NotNull
    @Column(name = "set_limit")
    private Integer setLimit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", columnDefinition = "TINYINT")
    private MeetingPinCategory meetingPinCategory;

    @OneToMany(mappedBy = "meetingPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingPinParticipants> meetingPinParticipants = new HashSet<>();

    @OneToMany(mappedBy = "requestMeetingPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingPinRequest> meetingPinRequests = new HashSet<>();


    @Builder
    public MeetingPin(User createUser, Double latitude, Double longitude, LocalDateTime dateTime, Gender gender, Integer maxAge, Integer minAge, String content, Integer setLimit, MeetingPinCategory meetingPinCategory) {
        this.createUser = createUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.gender = gender;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.content = content;
        this.setLimit = setLimit;
        this.meetingPinCategory = meetingPinCategory;
    }




    public enum MeetingPinCategory {
        study, walk
    }
}
