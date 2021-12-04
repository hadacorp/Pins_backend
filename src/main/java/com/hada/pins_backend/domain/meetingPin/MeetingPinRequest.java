package com.hada.pins_backend.domain.meetingPin;

import com.hada.pins_backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by bangjinhyuk on 2021/12/04.
 */
@Entity
@Getter
@NoArgsConstructor
@ToString
public class MeetingPinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String greetings;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestMeetingPinUser;

    @ManyToOne
    @JoinColumn(name = "meeting_pin_id")
    private MeetingPin requestMeetingPin;

    @Builder
    public MeetingPinRequest(String greetings, User requestMeetingPinUser, MeetingPin requestMeetingPin) {
        this.greetings = greetings;
        this.requestMeetingPinUser = requestMeetingPinUser;
        this.requestMeetingPin = requestMeetingPin;
    }
}
