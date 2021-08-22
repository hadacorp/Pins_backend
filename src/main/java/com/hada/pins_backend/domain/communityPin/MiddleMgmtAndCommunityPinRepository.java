package com.hada.pins_backend.domain.communityPin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiddleMgmtAndCommunityPinRepository extends JpaRepository<MiddleMgmtAndCommunityPin,Long> {
    List<MiddleMgmtAndCommunityPin> findMiddleMgmtAndCommunityPinsByCommunityPin_Id (Long communityPinId);

}
