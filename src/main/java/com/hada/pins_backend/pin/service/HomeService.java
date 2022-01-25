package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import com.hada.pins_backend.pin.model.response.HomePinResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
public interface HomeService {
    ResponseEntity<ApiResponse<List<HomeLocationResponse>>> searchLocation(String keyword);

    ResponseEntity<ApiResponse<List<HomePinResponse>>> loadPin(User user, HomePinRequest homePinRequest);

}
