package com.hada.pins_backend.service.home;

import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.dto.home.response.HomePinResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/28.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
    private static double latitudeRange = 0.0075;
    private static double longitudeRange = 0.004;
    private final MeetingPinRepository meetingPinRepository;

    @Override
    public ResponseEntity<List<HomePinResponse>> loadPin(String phoneNum, double latitude, double longitude) {
        double maxLatitude = latitude+latitudeRange, minLatitude = latitude-latitudeRange;
        double maxLongitude = longitude+longitudeRange, minLongitude = longitude-longitudeRange;
        List<MeetingPin> meetingPins =meetingPinRepository.findByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(maxLatitude,minLatitude,maxLongitude,minLongitude);
        System.out.println("<-------------->");
        meetingPins.forEach(System.out::println);
        return null;
    }
}
