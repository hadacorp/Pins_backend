package com.hada.pins_backend.pin.repository.meetingPin;

import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bangjinhyuk on 2022/03/12.
 */
@Repository
public interface MeetingPinParticipantsRepository extends JpaRepository<MeetingPinParticipants,Long> {
}
