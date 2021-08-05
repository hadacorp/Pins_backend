package com.hada.pins_backend.domain.storyPin;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bangjinhyuk on 2021/08/05.
 */
public interface StoryPinLikeRepository extends JpaRepository<StoryPinLike,Long> {
}
