package com.hada.pins_backend.domain.communityPin;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by bangjinhyuk on 2021/08/01.
 */
@Data
@NoArgsConstructor
@Entity
public class MembersAndCommunityPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
