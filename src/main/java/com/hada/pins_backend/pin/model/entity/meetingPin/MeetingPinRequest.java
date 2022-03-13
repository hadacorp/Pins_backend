package com.hada.pins_backend.pin.model.entity.meetingPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import com.hada.pins_backend.pin.model.enumable.State;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2022/03/12.
 */
@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "meeting_pin_request")
public class MeetingPinRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private MeetingPin requestMeetingPin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requestUser;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", columnDefinition = "TINYINT")
    private State state;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

    @Builder
    public MeetingPinRequest(MeetingPin requestMeetingPin, User requestUser, State state, String content) {
        this.requestMeetingPin = requestMeetingPin;
        this.requestUser = requestUser;
        this.state = state;
        this.content = content;
    }

}
