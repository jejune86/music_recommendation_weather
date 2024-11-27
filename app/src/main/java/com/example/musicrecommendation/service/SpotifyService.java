package com.example.musicrecommendation.service;


import android.util.Log;

import com.example.musicrecommendation.api.SpotifyAPI;
import com.example.musicrecommendation.data.model.spotify.SpotifyRecommendationResponse;
import com.example.musicrecommendation.data.model.weather.Weather;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotifyService {
    private static final String TAG = "SpotifyService";
    private SpotifyAPIInterface spotifyAPIInterface;

    public SpotifyService() {
        SpotifyAPI spotifyAPI = new SpotifyAPI();
        spotifyAPIInterface = spotifyAPI.getSpotifyAPIInterface();
    }
    
    // WeatherCallback 인터페이스 정의
    public interface SpotifyCallback {
        void onSpotifyRecommendationReceived();
    }

    public void getSpotifyRecommendation(String token, SpotifyCallback callback) {
        Weather weather = Weather.getInstance();
        
        // 날씨 기반 장르 선택
        String genre = getGenreBasedOnWeather(weather);
        
        // 날씨 기반 음악 특성 범위 설정
        Range valenceRange = getValenceRange(weather);
        Range energyRange = getEnergyRange(weather);
        Range tempoRange = getTempoRange(weather);
        
        spotifyAPIInterface.getRecommendation(
            "Bearer " + token,
            genre,                    // 선택된 장르
            1,                      // 추천 트랙 수
            "KR",                     // 시장
            0.0,                      // min_acousticness
            1.0,                      // max_acousticness
            0.0,                      // min_danceability
            1.0,                      // max_danceability
            energyRange.min,          // min_energy
            energyRange.max,          // max_energy
            valenceRange.min,         // min_valence
            valenceRange.max,         // max_valence
            0,                        // min_popularity
            100                       // max_popularity
        ).enqueue(new Callback<SpotifyRecommendationResponse>() {
            @Override
            public void onResponse(Call<SpotifyRecommendationResponse> call, Response<SpotifyRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processSpotifyRecommendation(response.body());
                    callback.onSpotifyRecommendationReceived();
                }
            }

            @Override
            public void onFailure(Call<SpotifyRecommendationResponse> call, Throwable t) {
                Log.e(TAG, "Spotify API 호출 실패: " + t.getMessage());
            }
        });
    }

    private static class Range {
        double min;
        double max;
        
        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    private Range getValenceRange(Weather weather) {
        if (weather.getSky() <= 5) {
            return new Range(0.6, 1.0);  // 맑은 날씨: 높은 밝은 분위기
        } else if (weather.getSky() <= 8) {
            return new Range(0.4, 0.7);  // 구름 많음: 중간 정도의 분위기
        } else {
            return new Range(0.0, 0.4);  // 흐림: 어두운 분위기
        }
    }

    private Range getEnergyRange(Weather weather) {
        if (weather.getPrecipitationType() > 0) {
            return new Range(0.0, 0.4);  // 비/눈: 낮은 에너지
        } else if (weather.getSky() <= 5) {
            return new Range(0.6, 1.0);  // 맑음: 높은 에너지
        } else {
            return new Range(0.3, 0.7);  // 흐림: 중간 에너지
        }
    }

    private Range getTempoRange(Weather weather) {
        if (weather.getSky() <= 5 && weather.getPrecipitationType() == 0) {
            return new Range(120, 180);  // 맑음: 빠른 템포
        } else if (weather.getPrecipitationType() > 0) {
            return new Range(60, 100);   // 비/눈: 느린 템포
        } else {
            return new Range(90, 130);   // 흐림: 중간 템포
        }
    }

    private String getGenreBasedOnWeather(Weather weather) {
        if (weather.getPrecipitationType() > 0) {
            return "rainy-day";
        } else if (weather.getSky() > 8) {
            return "cloudy-day";
        } else {
            return "sunny-day";
        }
    }

    private void processSpotifyRecommendation(SpotifyRecommendationResponse response) {
        if (response != null && response.getTracks() != null) {
            List<SpotifyRecommendationResponse.Track> tracks = response.getTracks();
            for (SpotifyRecommendationResponse.Track track : tracks) {
                Log.d(TAG, "Recommended Track: " + track.getName() + " - " + track.getArtists().get(0).getName());

            }
        } else {
            Log.e(TAG, "Spotify recommendation empty");
        }
    }

}
