package com.example.pins_backend.domain.meetingPin;

import com.example.pins_backend.domain.storyPin.StoryPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPinRepository extends JpaRepository<MeetingPin,Long> {

}
