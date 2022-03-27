package com.hada.pins_backend.pin.repository.storyPin;

import com.hada.pins_backend.model.LongitudeAndLatitude;
import com.hada.pins_backend.pin.model.entity.storyPin.QStoryPin;
import com.hada.pins_backend.pin.model.entity.storyPin.StoryPin;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Created by bangjinhyuk on 2022/03/19.
 * Modified by bangjinhyuk on 2022/03/27.
 */
@Repository
public class StoryPinRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private QStoryPin qStoryPin = QStoryPin.storyPin;

    public StoryPinRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<StoryPin> findAllStoryPinAtHome(LongitudeAndLatitude longitudeAndLatitude,
                                                HomePinRequest homePinRequest) {

        BooleanBuilder searchCondition = new BooleanBuilder();

        if(Objects.nonNull(homePinRequest.getStoryPinCategory())){
            searchCondition.and(qStoryPin.category.in(homePinRequest.getStoryPinCategory()));
        }
        if(Objects.nonNull(homePinRequest.getKeyword())){
            searchCondition.and(qStoryPin.content.contains(homePinRequest.getKeyword()));
        }

        return jpaQueryFactory.select(qStoryPin).from(qStoryPin)
                .where(qStoryPin.latitude.between(longitudeAndLatitude.getMinLatitude(), longitudeAndLatitude.getMaxLatitude()))
                .where(qStoryPin.longitude.between(longitudeAndLatitude.getMinLongitude(),longitudeAndLatitude.getMaxLongitude()))
                .where(searchCondition)
                .fetch();
    }
}
