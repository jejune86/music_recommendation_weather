package com.example.musicrecommendation.api;

import com.example.musicrecommendation.service.SpotifyAPIInterface;
import com.example.musicrecommendation.service.WeatherAPIInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyAPI {
    private static final String BASE_URL = "https://api.spotify.com/v1/";

    private final Retrofit retrofit;

    public SpotifyAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public SpotifyAPIInterface getSpotifyAPIInterface() {
        return retrofit.create(SpotifyAPIInterface.class);
    }
}
