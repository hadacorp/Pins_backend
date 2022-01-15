package com.hada.pins_backend.pin.repository;

import com.hada.pins_backend.pin.model.entity.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
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
