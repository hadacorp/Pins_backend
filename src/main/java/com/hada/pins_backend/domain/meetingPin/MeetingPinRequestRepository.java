package com.hada.pins_backend.domain.meetingPin;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bangjinhyuk on 2021/12/04.
 */
public interface MeetingPinRequestRepository extends JpaRepository<MeetingPinRequest,Long> {
}
