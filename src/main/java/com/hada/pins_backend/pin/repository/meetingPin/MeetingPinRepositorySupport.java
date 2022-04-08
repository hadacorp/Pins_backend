package com.hada.pins_backend.pin.repository.meetingPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.QMeetingPin;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by bangjinhyuk on 2022/01/22.
 * Modified by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/27.
 * Modified by parksuho on 2022/04/08.
 */
@Repository
public class MeetingPinRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private QMeetingPin qMeetingPin = QMeetingPin.meetingPin;

    public MeetingPinRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<MeetingPin> findAllMeetingPinAtHome(LongitudeAndLatitude longitudeAndLatitude,
                                                    User user,
                                                    HomePinRequest homePinRequest) {
        BooleanBuilder searchCondition = new BooleanBuilder();
        BooleanBuilder searchDateCondition = new BooleanBuilder();

        if(Objects.nonNull(homePinRequest.getMeetDate())) {
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;
            for (LocalDate meetDate : homePinRequest.getMeetDate()) {
                if(Objects.nonNull(homePinRequest.getStartMeetTime()))
                    startDateTime = meetDate.atTime(homePinRequest.getStartMeetTime());
                else
                    startDateTime = meetDate.atTime(LocalTime.MIN);
                if(Objects.nonNull(homePinRequest.getStartMeetTime()))
                    endDateTime = meetDate.atTime(homePinRequest.getEndMeetTime());
                else
                    endDateTime = meetDate.atTime(LocalTime.MIN);
                searchDateCondition.or(qMeetingPin.dateTime.between(startDateTime, endDateTime));
            }
        }
        if(Objects.nonNull(homePinRequest.getMaxAge()) && Objects.nonNull(homePinRequest.getMinAge())){
            searchCondition.and(qMeetingPin.createUser.age.between(homePinRequest.getMinAge(), homePinRequest.getMaxAge()));
        }
        if(Objects.nonNull(homePinRequest.getMeetGender())){
            if(homePinRequest.getMeetGender() != Gender.Both)
                searchCondition.and(qMeetingPin.genderLimit.eq(homePinRequest.getMeetGender()));
        }
        if(Objects.nonNull(homePinRequest.getMeetingPinCategory())){
            searchCondition.and(qMeetingPin.category.in(homePinRequest.getMeetingPinCategory()));
        }
        if(Objects.nonNull(homePinRequest.getKeyword())){
            searchCondition.and(qMeetingPin.content.contains(homePinRequest.getKeyword()));
        }

        return jpaQueryFactory.select(qMeetingPin).from(qMeetingPin)
                .where(qMeetingPin.latitude.between(longitudeAndLatitude.getMinLatitude(), longitudeAndLatitude.getMaxLatitude()))
                .where(qMeetingPin.longitude.between(longitudeAndLatitude.getMinLongitude(),longitudeAndLatitude.getMaxLongitude()))
                .where(qMeetingPin.maxAge.goe(user.getAge()).and(qMeetingPin.minAge.loe(user.getAge())))
                .where(qMeetingPin.genderLimit.eq(user.getGender()).or(qMeetingPin.genderLimit.eq(Gender.Both)))
//                .where(qMeetingPin.dateTime.before(LocalDateTime.now()))
                .where(searchCondition)
                .fetch();
    }
}
