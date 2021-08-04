package com.example.pins_backend.domain.communityPin;

import com.example.pins_backend.domain.meetingPin.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiddleMgmtAndCommunityPinRepository extends JpaRepository<MiddleMgmtAndCommunityPin,Long> {
}
