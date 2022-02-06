package com.hada.pins_backend.pin.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.pin.model.request.HomePinRequest;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import com.hada.pins_backend.pin.model.response.HomePinResponse;
import com.hada.pins_backend.pin.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/home")
public class HomeController {
    private final HomeService homeService;

    /**
     * 홈화면 핀 , 카드뷰 로딩
     */
    @GetMapping("/pin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<HomePinResponse>> loadPin(
            HomePinRequest homePinRequest,
            @CurrentUser User user
    ){

        return new ApiResponse<>(homeService.loadPin(user,homePinRequest));
    }

    /**
     * 키워드 장소 검색
     */
    @GetMapping("/search/location")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<HomeLocationResponse>> searchLocation(@RequestParam String keyword) throws ParseException {
        return new ApiResponse<>(homeService.searchLocation(keyword));
    }
}
