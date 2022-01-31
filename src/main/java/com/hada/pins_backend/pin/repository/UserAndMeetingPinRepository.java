package com.hada.pins_backend.pin.repository;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.pin.model.entity.MeetingPin;
import com.hada.pins_backend.pin.model.entity.UserAndMeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAndMeetingPinRepository extends JpaRepository<UserAndMeetingPin,Long> {
    Optional<UserAndMeetingPin> findByMemberAndMeetingPin(User member, MeetingPin meetingPin);
}
