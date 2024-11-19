package com.example.musicrecommendation.api;

import com.example.musicrecommendation.service.WeatherAPIInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPI {
    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    private Retrofit retrofit;

    public WeatherAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public WeatherAPIInterface getWeatherAPIInterface() {
        return retrofit.create(WeatherAPIInterface.class);
    }
}
