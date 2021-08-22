package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.user.User;
import lombok.*;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Getter
@ToString
@NoArgsConstructor
@Entity
public class MiddleMgmtAndCommunityPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User middleManager;

    @ManyToOne
    @JoinColumn(name = "community_pin_id")
    private CommunityPin communityPin;

    @Builder
    public MiddleMgmtAndCommunityPin(User middleManager,
                                     CommunityPin communityPin){
        this.middleManager = middleManager;
        this.communityPin = communityPin;
    }
}
