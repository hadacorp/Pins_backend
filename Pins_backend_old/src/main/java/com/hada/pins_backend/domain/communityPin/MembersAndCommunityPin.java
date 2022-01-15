package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.BaseTimeEntity;
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
public class MembersAndCommunityPin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User communityMember;

    @ManyToOne
    @JoinColumn(name = "community_pin_id")
    private CommunityPin communityPin;

    @Builder
    public MembersAndCommunityPin(User communityMember,
                                  CommunityPin communityPin){
        this.communityMember = communityMember;
        this.communityPin = communityPin;
    }
}
