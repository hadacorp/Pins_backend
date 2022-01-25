package com.hada.pins_backend.pin.repository;

import com.hada.pins_backend.pin.model.entity.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
@Repository
public interface MeetingPinRepository extends JpaRepository<MeetingPin,Long> {

}
