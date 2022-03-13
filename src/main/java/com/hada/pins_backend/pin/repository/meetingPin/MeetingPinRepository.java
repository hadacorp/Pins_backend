package com.hada.pins_backend.pin.repository.meetingPin;

import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
@Repository
public interface MeetingPinRepository extends JpaRepository<MeetingPin,Long> {

}
