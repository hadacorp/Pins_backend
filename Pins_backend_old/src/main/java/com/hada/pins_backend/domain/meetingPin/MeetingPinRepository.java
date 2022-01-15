package com.hada.pins_backend.domain.meetingPin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingPinRepository extends JpaRepository<MeetingPin,Long> {

    /**
     * 지정 위도 경도 내 핀 가져오기 + 나이 고려
     */
    List<MeetingPin> findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqualAndMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(double maxLatitude,
                                                                                                                                 double minLatitude,
                                                                                                                                 double maxLongitude,
                                                                                                                                 double minLongitude, int userAge_1, int userAge_2);
    /**
     * 키워드용 핀 가져오기 + 나이 고려
     */
    List<MeetingPin> findByMaxAgeGreaterThanEqualAndMinAgeLessThanEqual(int userAge_1, int userAge_2);


}