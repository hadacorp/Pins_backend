package com.hada.pins_backend.pin.model.entity;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@NoArgsConstructor
@Entity
@ToString
public class UserAndMeetingPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name = "meeting_pin_id")
    private MeetingPin meetingPin;

    @Builder
    public UserAndMeetingPin(User member, MeetingPin meetingPin) {
        this.member = member;
        this.meetingPin = meetingPin;
    }



}
