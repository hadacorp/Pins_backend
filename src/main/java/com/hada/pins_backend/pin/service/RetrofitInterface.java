package com.hada.pins_backend.pin.service;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by bangjinhyuk on 2022/04/02.
 */
public interface RetrofitInterface {

    @GET("/v2/local/search/keyword.json")
    Call<Object> getLocations(@Header("Authorization") String kakaoKey, @Query("query") String keyword);
}
