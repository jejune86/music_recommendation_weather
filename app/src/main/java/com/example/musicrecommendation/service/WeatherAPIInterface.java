package com.example.musicrecommendation.service;
import com.example.musicrecommendation.data.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WeatherAPIInterface {
    @GET("1360000/VilageFcstInfoService_2.0/getVilageFcst")
    Call<WeatherResponse> getWeather(
            @Query("serviceKey") String serviceKey,
            @Query("numOfRows") String numOfRows,
            @Query("pageNo") String pageNo,
            @Query("dataType") String dataType,
            @Query("base_date") String baseDate,
            @Query("base_time") String baseTime,
            @Query("nx") int nx,
            @Query("ny") int ny
    );
}