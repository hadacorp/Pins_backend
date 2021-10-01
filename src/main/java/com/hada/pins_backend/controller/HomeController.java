package com.hada.pins_backend.controller;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.home.FilterData;
import com.hada.pins_backend.dto.home.LongitudeAndLatitude;
import com.hada.pins_backend.dto.home.response.HomeCardViewResponse;
import com.hada.pins_backend.dto.home.response.HomeLocationResponse;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import com.hada.pins_backend.dto.user.UserLoginForm;
import com.hada.pins_backend.service.home.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    /**
     * 홈화면 핀 로딩 deprecated
     */
    @GetMapping("/pin")
    public ResponseEntity<List<HomePinResponse>> loadPin(@RequestParam double latitude,
                                                         @RequestParam double longitude,
                                                         @RequestParam String meetingPinCategory,
                                                         @RequestParam String meetDate,
                                                         @RequestParam String meetTime,
                                                         @RequestParam String meetGender,
                                                         @RequestParam String meetAge,
                                                         @RequestParam String communityPinCategory,
                                                         @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);
        return homeService.loadPin(user.getPhoneNum(),new LongitudeAndLatitude(latitude,longitude),filterData);
    }
    /**
     * 홈화면 카드뷰 로딩 deprecated
     */
    @GetMapping("/cardview")
    public ResponseEntity<List<HomeCardViewResponse>> loadCardView(@RequestParam String pinType,
                                                                   @RequestParam Long pinDBId,
                                                                   @RequestParam String meetingPinCategory,
                                                                   @RequestParam String meetDate,
                                                                   @RequestParam String meetTime,
                                                                   @RequestParam String meetGender,
                                                                   @RequestParam String meetAge,
                                                                   @RequestParam String communityPinCategory,
                                                                   @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);

        return homeService.loadCardView(user.getPhoneNum(),pinType,pinDBId,filterData);
    }

    /**
     * 홈화면 핀 , 카드뷰 로딩
     */
    @GetMapping("/pinandcardview")
    public ResponseEntity<List<HomeCardViewResponse>> loadPinAndCardview(@RequestParam double latitude,
                                                                         @RequestParam double longitude,
                                                                         @RequestParam String meetingPinCategory,
                                                                         @RequestParam String meetDate,
                                                                         @RequestParam String meetTime,
                                                                         @RequestParam String meetGender,
                                                                         @RequestParam String meetAge,
                                                                         @RequestParam String communityPinCategory,
                                                                         @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);

        return homeService.loadPinAndCardview(user.getPhoneNum(),new LongitudeAndLatitude(latitude,longitude),filterData);
    }

    /**
     * 홈화면  핀 , 키워드 카드뷰 로딩
     */
    @GetMapping("/search/pinandcardview")
    public ResponseEntity<List<HomeCardViewResponse>> searchPinAndCardview(@RequestParam double latitude,
                                                                           @RequestParam double longitude,
                                                                           @RequestParam String keyword,
                                                                           @RequestParam String meetingPinCategory,
                                                                           @RequestParam String meetDate,
                                                                           @RequestParam String meetTime,
                                                                           @RequestParam String meetGender,
                                                                           @RequestParam String meetAge,
                                                                           @RequestParam String communityPinCategory,
                                                                           @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);
        return homeService.searchPinAndCardview(user.getPhoneNum(),new LongitudeAndLatitude(latitude,longitude),keyword,filterData);
    }

    /**
     * 홈화면 키워드 카드뷰 로딩 deprecated
     */
    @GetMapping("/search/cardview")
    public ResponseEntity<List<HomeCardViewResponse>> searchCardView(@RequestParam String pinType,
                                                                   @RequestParam Long pinDBId,
                                                                   @RequestParam String keyword,
                                                                   @RequestParam String meetingPinCategory,
                                                                   @RequestParam String meetDate,
                                                                   @RequestParam String meetTime,
                                                                   @RequestParam String meetGender,
                                                                   @RequestParam String meetAge,
                                                                   @RequestParam String communityPinCategory,
                                                                   @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);
        return homeService.searchCardView(user.getPhoneNum(),pinType,pinDBId,keyword,filterData);
    }


    /**
     * 키워드 핀 검색 deprecated
     */
    @GetMapping("/search/pin")
    public ResponseEntity<List<HomePinResponse>> searchPin(@RequestParam String keyword,
                                                           @RequestParam double latitude,
                                                           @RequestParam double longitude,
                                                           @RequestParam String meetingPinCategory,
                                                           @RequestParam String meetDate,
                                                           @RequestParam String meetTime,
                                                           @RequestParam String meetGender,
                                                           @RequestParam String meetAge,
                                                           @RequestParam String communityPinCategory,
                                                           @RequestParam String storyPinCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        FilterData filterData = new FilterData(meetingPinCategory,meetDate,meetTime,meetGender,meetAge,communityPinCategory,storyPinCategory);
        return homeService.searchPin(user.getPhoneNum(),keyword,new LongitudeAndLatitude(latitude,longitude),filterData);
    }
    /**
     * 키워드 장소 검색
     */
    @GetMapping("/search/location")
    public ResponseEntity<List<HomeLocationResponse>> searchLocation(@RequestParam String keyword){
        return homeService.searchLocation(keyword);
    }
}
