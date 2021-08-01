package com.example.pins_backend.domain.communityPost;

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
public class CommunityPostComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
