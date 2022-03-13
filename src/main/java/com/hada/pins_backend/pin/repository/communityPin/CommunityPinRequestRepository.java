package com.hada.pins_backend.pin.repository.communityPin;

import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bangjinhyuk on 2022/03/13.
 */
@Repository
public interface CommunityPinRequestRepository extends JpaRepository<CommunityPinRequestRepository, Long> {
}
