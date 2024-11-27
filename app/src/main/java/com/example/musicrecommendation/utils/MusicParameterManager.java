package com.example.musicrecommendation.utils;

import android.util.Range;

import com.example.musicrecommendation.data.model.weather.Weather;

public class MusicParameterManager {
    private Range<Float> energyRange;
    private Range<Float> valenceRange;
    private Range<Float> danceabilityRange;
    private Range<Float> acousticnessRange;
    
    public void updateRangesBasedOnWeather(Weather weather) {
        // 날씨에 따른 기본값 설정
        if (weather.getSky() <= 5) {  // 맑음
            energyRange = new Range<Float>(0.6f, 1.0f);
            valenceRange = new Range<Float>(0.6f, 1.0f);
        } else if (weather.getSky() <= 8) {  // 구름 많음
            energyRange = new Range<Float>(0.3f, 0.7f);
            valenceRange = new Range<Float>(0.4f, 0.7f);
        } else {  // 흐림
            energyRange = new Range<Float>(0.0f, 0.4f);
            valenceRange = new Range<Float>(0.0f, 0.4f);
        }
    }
    public Range<Float> getEnergyRange() {
        return energyRange;
    }

    public void setEnergyRange(Range<Float> energyRange) {
        this.energyRange = energyRange;
    }

    public Range<Float> getValenceRange() {
        return valenceRange;
    }

    public void setValenceRange(Range<Float> valenceRange) {
        this.valenceRange = valenceRange;
    }

    public Range<Float> getDanceabilityRange() {
        return danceabilityRange;
    }

    public void setDanceabilityRange(Range<Float> danceabilityRange) {
        this.danceabilityRange = danceabilityRange;
    }

    public Range<Float> getAcousticnessRange() {
        return acousticnessRange;
    }

    public void setAcousticnessRange(Range<Float> acousticnessRange) {
        this.acousticnessRange = acousticnessRange;
    }
}