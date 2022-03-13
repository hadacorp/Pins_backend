package com.hada.pins_backend.pin.repository.meetingPin;

import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.QMeetingPin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/22.
 */
@Repository
public class MeetingPinRepositorySupport{
    private final JPAQueryFactory jpaQueryFactory;

    private QMeetingPin qMeetingPin = QMeetingPin.meetingPin;

    public MeetingPinRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<MeetingPin> findAllMeetingPinAtHome(LongitudeAndLatitude longitudeAndLatitude,
                                                    int age,
                                                    Gender gender) {
        return jpaQueryFactory.select(qMeetingPin).from(qMeetingPin)
                .where(qMeetingPin.latitude.between(longitudeAndLatitude.getMinLatitude(), longitudeAndLatitude.getMaxLatitude()))
                .where(qMeetingPin.longitude.between(longitudeAndLatitude.getMinLongitude(),longitudeAndLatitude.getMaxLongitude()))
                .where(qMeetingPin.maxAge.goe(age).and(qMeetingPin.minAge.loe(age)))
                .where(qMeetingPin.gender.eq(gender).or(qMeetingPin.gender.eq(Gender.Both)))
                .fetch();
    }
}
