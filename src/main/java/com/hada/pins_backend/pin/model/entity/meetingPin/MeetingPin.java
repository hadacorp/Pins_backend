package com.hada.pins_backend.pin.model.entity.meetingPin;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.handler.ListToStringConverter;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/04/06.
 * Modified by parksuho on 2022/04/08.
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "meeting_pin")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
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
//    @Column(name = "gender", columnDefinition = "TINYINT")
    private Gender genderLimit;

    @NotNull
    @Column(name = "max_age")
    private Integer maxAge;

    @NotNull
    @Column(name = "min_age")
    private Integer minAge;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

    @Convert(converter = ListToStringConverter.class)
    private List<String> images = new ArrayList<>();

    @NotNull
    @Column(name = "set_limit")
    private Integer setLimit;

    @NotNull
    @Enumerated(EnumType.STRING)
//    @Column(name = "category", columnDefinition = "TINYINT")
    private MeetingPinCategory category;

    @OneToMany(mappedBy = "requestMeetingPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingPinRequest> meetingPinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "meetingPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingPinParticipants> meetingPinParticipants = new ArrayList<>();

    @Builder
    public MeetingPin(User createUser, Double latitude, Double longitude, LocalDateTime dateTime,
                      Gender genderLimit, Integer maxAge, Integer minAge, String content,
                      Integer setLimit, MeetingPinCategory category) {
        this.createUser = createUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.genderLimit = genderLimit;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.content = content;
        this.setLimit = setLimit;
        this.category = category;
    }

    public enum MeetingPinCategory {
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
        ETC
    }

    public void updateImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String image) { this.images.add(image); }

    public void addRequest(MeetingPinRequest request) { this.meetingPinRequests.add(request); }

    public void addParticipant(MeetingPinParticipants participants) { this.meetingPinParticipants.add(participants); }

    public boolean checkLimit() {
        return meetingPinParticipants.size() == setLimit;
    }
}
