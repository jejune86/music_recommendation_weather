package com.example.musicrecommendation.api;

import com.example.musicrecommendation.service.WeatherAPIInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPI {
    private static final String BASE_URL = "https://apis.data.go.kr/";

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
