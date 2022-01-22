package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.model.ApiResponse;
import com.hada.pins_backend.pin.model.response.HomeLocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    @Value("${kakao.key}")
    private String kakaoKey;

    @Override
    public ResponseEntity<ApiResponse<List<HomeLocationResponse>>> searchLocation(String keyword) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("Authorization",kakaoKey)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req,String.class);
        List<HomeLocationResponse> homeLocationResponses = new ArrayList<>();

        try {
            JSONObject responseJson = new JSONObject(result.getBody());
            JSONArray locations = responseJson.getJSONArray("documents");
            for (int i = 0; i < locations.length(); i++) {
                JSONObject data = locations.getJSONObject(i);

                HomeLocationResponse homeLocationResponse = HomeLocationResponse.builder()
                        .placeName(data.getString("place_name"))
                        .longitude(data.getDouble("x"))
                        .latitude(data.getDouble("y"))
                        .build();
                homeLocationResponses.add(homeLocationResponse);
            }
        }catch (JSONException e){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(homeLocationResponses));
    }
}
