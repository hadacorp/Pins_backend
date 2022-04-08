package com.hada.pins_backend.pin.model.entity.communityPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.BaseTimeEntity;
import com.hada.pins_backend.pin.model.enumable.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bangjinhyuk on 2022/03/13.
 * Created by bangjinhyuk on 2022/04/05.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "community_pin_participants")
public class CommunityPinParticipants extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private CommunityPin communityPin;

    @NotNull
    @Column(name = "position", columnDefinition = "TINYINT")
    @Enumerated(EnumType.ORDINAL)
    private Position position;

    @Builder
    public CommunityPinParticipants(User member, CommunityPin communityPin, Position position) {
        this.member = member;
        this.communityPin = communityPin;
        this.position = position;
    }
}
