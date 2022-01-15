package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPinRepository extends JpaRepository<CommunityPin,Long> {

    /**
     * 지정 위도 경도 내 핀 가져오기 + 나이 고려
     */
    List<CommunityPin> findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqualAndMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(double maxLatitude,
                                                                                                                                                                                double minLatitude,
                                                                                                                                                                                double maxLongitude,
                                                                                                                                                                                double minLongitude,
                                                                                                                                                                                int userAge_1,
                                                                                                                                                                                int userAge_2);


}
