package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
public interface HomeService {
    ResponseEntity<ApiResponse<List<HomeLocationResponse>>> searchLocation(String keyword);
}
