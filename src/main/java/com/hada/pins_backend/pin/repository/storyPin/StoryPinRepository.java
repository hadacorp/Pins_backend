package com.hada.pins_backend.pin.repository.storyPin;

import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bangjinhyuk on 2022/03/19.
 */
@Repository
public interface StoryPinRepository extends JpaRepository<StoryPin, Long> {
}
