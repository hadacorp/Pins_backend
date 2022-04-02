package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by bangjinhyuk on 2022/03/19.
 */
public interface HomeService {
    List<HomeLocationResponse.LocationResponse> searchLocation(String keyword) throws ParseException;

    Map<String,List> loadPin(User user, HomePinRequest homePinRequest);

}
