package com.example.musicrecommendation.service;

import com.example.musicrecommendation.data.model.spotify.SpotifyRecommendationResponse;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SpotifyAPIInterface {
    @GET("recommendations/available-genre-seeds")
    Call<JsonObject> getAvailableGenres(@Header("Authorization") String token);

    @GET("recommendations")
    Call<SpotifyRecommendationResponse> getRecommendation(
        @Header("Authorization") String token,
        @Query("seed_genres") String seedGenres,
        @Query("limit") int limit,
        @Query("market") String market,
        @Query("min_acousticness") double minAcousticness,
        @Query("max_acousticness") double maxAcousticness,
        @Query("min_danceability") double minDanceability,
        @Query("max_danceability") double maxDanceability,
        @Query("min_energy") double minEnergy,
        @Query("max_energy") double maxEnergy,
        @Query("min_valence") double minValence,
        @Query("max_valence") double maxValence,
        @Query("min_popularity") int minPopularity,
        @Query("max_popularity") int maxPopularity
    );
}