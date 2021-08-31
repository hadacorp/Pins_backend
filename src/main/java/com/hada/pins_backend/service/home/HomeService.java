package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
public interface HomeService {
    ResponseEntity<List<HomePinResponse>>loadPin(String phoneNum,double latitude, double logitude);
}
