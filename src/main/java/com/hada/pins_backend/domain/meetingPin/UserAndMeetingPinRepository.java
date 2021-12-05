package com.hada.pins_backend.domain.meetingPin;

import com.hada.pins_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAndMeetingPinRepository extends JpaRepository<UserAndMeetingPin,Long> {
    Optional<UserAndMeetingPin> findByMemberAndMeetingPin(User member, MeetingPin meetingPin);
}
