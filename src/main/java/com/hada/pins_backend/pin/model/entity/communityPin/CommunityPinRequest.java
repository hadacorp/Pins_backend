package com.hada.pins_backend.pin.model.entity.communityPin;

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
 * Created by bangjinhyuk on 2022/03/13.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "community_pin_request")
public class CommunityPinRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requestUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private CommunityPin requestCommunityPin;

    @NotNull
    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    @NotNull @NotBlank
    @Column(name = "content")
    private String content;

    @Builder
    public CommunityPinRequest(User requestUser, CommunityPin requestCommunityPin, State state, String content) {
        this.requestUser = requestUser;
        this.requestCommunityPin = requestCommunityPin;
        this.state = state;
        this.content = content;
    }
}
