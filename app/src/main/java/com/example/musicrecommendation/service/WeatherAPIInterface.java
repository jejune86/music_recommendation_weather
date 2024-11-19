package com.example.musicrecommendation.service;
import com.example.musicrecommendation.data.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WeatherAPIInterface {
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("serviceKey") String serviceKey,  // 인증키
            @Query("dataType") String dataType,     // 응답자료형식 (XML/JSON)
            @Query("base_date") String baseDate,    // 발표일자
            @Query("base_time") String baseTime,    // 발표시각
            @Query("nx") double nx,                 // 예보지점 X 좌표 (위도)
            @Query("ny") double ny
    );
}