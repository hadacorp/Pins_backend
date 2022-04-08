package com.hada.pins_backend.pin.model.entity.meetingPin;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2022/03/12.
 * Modified by parksuho on 2022/04/08.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "meeting_pin_participants")
public class MeetingPinParticipants extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private MeetingPin meetingPin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public MeetingPinParticipants(MeetingPin meetingPin, User user) {
        this.meetingPin = meetingPin;
        this.user = user;
    }
}
