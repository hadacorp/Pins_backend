package com.hada.pins_backend.domain.meetingPin;

import com.hada.pins_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by bangjinhyuk on 2021/12/04.
 */
public interface MeetingPinRequestRepository extends JpaRepository<MeetingPinRequest,Long> {
    Optional<MeetingPinRequest> findByRequestMeetingPinAndRequestMeetingPinUser(MeetingPin meetingPin, User user);
}
