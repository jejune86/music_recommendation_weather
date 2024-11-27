package com.example.musicrecommendation.data.model.weather;

public class Weather {
    private static Weather instance;
    private double temperature;
    private int sky; //~5 맑음, ~8 구름 많음, ~10 흐림
    private int precipitationType; //강수형태 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private double precipitation; //강수량

    private Weather() {} // private 생성자

    public static Weather getInstance() {
        if (instance == null) {
            instance = new Weather();
        }
        return instance;
    }

    public static void setInstance(Weather instance) {
        Weather.instance = instance;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getSky() {
        return sky;
    }

    public void setSky(int sky) {
        this.sky = sky;
    }

    public int getPrecipitationType() {
        return precipitationType;
    }

    public void setPrecipitationType(int precipitationType) {
        this.precipitationType = precipitationType;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }
}
