package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembersAndCommunityPinRepository extends JpaRepository<MembersAndCommunityPin,Long> {
    List<MembersAndCommunityPin> findMembersAndCommunityPinsByCommunityPin_Id (Long communityPinId);
}
