package com.hada.pins_backend.pin.model.entity.meetingPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2022/03/12.
 */
@Entity
@Getter
@NoArgsConstructor
@ToString
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
    private User member;

    @Builder
    public MeetingPinParticipants(MeetingPin meetingPin, User member) {
        this.meetingPin = meetingPin;
        this.member = member;
    }
}
