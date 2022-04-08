package com.hada.pins_backend.pin.repository.communityPin;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.QCommunityPin;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Created by bangjinhyuk on 2022/03/13.
 * Modified by bangjinhyuk on 2022/03/27.
 * Modified by parksuho on 2022/04/09.
 */

@Repository
public class CommunityPinRepositorySupport{
    private final JPAQueryFactory jpaQueryFactory;

    private QCommunityPin qCommunityPin = QCommunityPin.communityPin;

    public CommunityPinRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<CommunityPin> findAllCommunityPinAtHome(LongitudeAndLatitude longitudeAndLatitude,
                                                    User user,
                                                    HomePinRequest homePinRequest) {

        BooleanBuilder searchCondition = new BooleanBuilder();

        if(Objects.nonNull(homePinRequest.getCommunityPinCategory())){
            searchCondition.and(qCommunityPin.category.in(homePinRequest.getCommunityPinCategory()));
        }
        if(Objects.nonNull(homePinRequest.getKeyword())){
            searchCondition.and(qCommunityPin.content.contains(homePinRequest.getKeyword()));
        }
        return jpaQueryFactory.select(qCommunityPin).from(qCommunityPin)
                .where(qCommunityPin.latitude.between(longitudeAndLatitude.getMinLatitude(), longitudeAndLatitude.getMaxLatitude()))
                .where(qCommunityPin.longitude.between(longitudeAndLatitude.getMinLongitude(),longitudeAndLatitude.getMaxLongitude()))
                .where(qCommunityPin.maxAge.goe(user.getAge()).and(qCommunityPin.minAge.loe(user.getAge())))
                .where(qCommunityPin.genderLimit.eq(user.getGender()).or(qCommunityPin.genderLimit.eq(Gender.Both)))
                .where(searchCondition)
                .fetch();
    }
}
