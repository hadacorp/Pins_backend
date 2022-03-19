package com.hada.pins_backend.pin.repository.communityPin;

import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.QCommunityPin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/03/13.
 */

@Repository
public class CommunityPinRepositorySupport{
    private final JPAQueryFactory jpaQueryFactory;

    private QCommunityPin qCommunityPin = QCommunityPin.communityPin;

    public CommunityPinRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<CommunityPin> findAllCommunityPinAtHome(LongitudeAndLatitude longitudeAndLatitude,
                                                    int age,
                                                    Gender gender) {
        return jpaQueryFactory.select(qCommunityPin).from(qCommunityPin)
                .where(qCommunityPin.latitude.between(longitudeAndLatitude.getMinLatitude(), longitudeAndLatitude.getMaxLatitude()))
                .where(qCommunityPin.longitude.between(longitudeAndLatitude.getMinLongitude(),longitudeAndLatitude.getMaxLongitude()))
                .where(qCommunityPin.maxAge.goe(age).and(qCommunityPin.minAge.loe(age)))
                .where(qCommunityPin.gender.eq(gender).or(qCommunityPin.gender.eq(Gender.Both)))
                .fetch();
    }
}
