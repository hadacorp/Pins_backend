package com.hada.pins_backend.domain.meetingPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
public class MeetingPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createUser;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    @NotBlank
    private String category;

    @Enumerated(value = EnumType.STRING)
    private Gender setGender;

    @NotBlank
    private String setAge;

    @DecimalMin(value = "1")
    private int setLimit;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @OneToMany(mappedBy = "meetingPin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserAndMeetingPin> userAndMeetingPins;

    @Builder
    public MeetingPin(User createUser,
                      String title,
                      String content,
                      String category,
                      Gender setGender,
                      String setAge,
                      int setLimit,
                      double longitude,
                      double latitude,
                      LocalDateTime date) {
        this.createUser = createUser;
        this.title = title;
        this.content = content;
        this.category = category;
        this.setGender = setGender;
        this.setAge = setAge;
        this.setLimit = setLimit;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
    }




}
