package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.communityPin.CommunityPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryPinRepository extends JpaRepository<StoryPin,Long> {

    /**
     * 지정 위도 경도 내 핀 가져오기 + 나이 고려
     */
    List<StoryPin> findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(double maxLatitude,
                                                                                                                                  double minLatitude,
                                                                                                                                  double maxLongitude,
                                                                                                                                  double minLongitude);

}
