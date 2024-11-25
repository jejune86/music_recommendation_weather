package com.example.musicrecommendation.service;


import android.util.Log;

import com.example.musicrecommendation.BuildConfig;
import com.example.musicrecommendation.api.WeatherAPI;
import com.example.musicrecommendation.data.model.GridXY;
import com.example.musicrecommendation.data.model.Weather;
import com.example.musicrecommendation.data.model.WeatherResponse;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService {
    private static final String TAG = "WeatherService";
    private WeatherAPIInterface weatherAPIInterface;

    public WeatherService() {
        WeatherAPI weatherAPI = new WeatherAPI();
        weatherAPIInterface = weatherAPI.getWeatherAPIInterface();
    }
    
    // WeatherCallback 인터페이스 정의
    public interface WeatherCallback {
        void onWeatherDataReceived();
    }

    public void getWeatherData(double latitude, double longitude, WeatherCallback callback) {
        GridXY grid = new GridXY(latitude, longitude);

        // Calculate the current date and the closest base time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d(TAG, "hour = "+hour);
        List<String> baseHours = List.of("02", "05", "08", "11", "14", "17", "20", "23");

        // Find the nearest previous base hour
        String baseHour = "-1";
        for (String bh : baseHours) {
            int bhInt = Integer.parseInt(bh);
            if (bhInt <= hour) {
                baseHour = bh;
            } else {
                break;
            }
        }
        int baseHourInt;
        if (baseHour.equals("-1")) {
            calendar.add(Calendar.DATE, -1);
            baseHour = baseHours.get(baseHours.size() - 1); // "23"
        }
        baseHourInt = Integer.parseInt(baseHour);


        calendar.set(Calendar.HOUR_OF_DAY, baseHourInt);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Set the date and time
        String baseDate = String.format("%tY%<tm%<td", calendar);
        String baseTime = String.format("%02d00", baseHourInt);
        Log.d(TAG, "baseDate = " + baseDate + "  baseTime = " + baseTime);
        Log.d(TAG, "X = " + grid.x + "  Y = " + grid.y);
        String serviceKey = BuildConfig.WEATHER_API_KEY;
        weatherAPIInterface.getWeather(
            serviceKey,
            "50",
            "1",
            "JSON",
            baseDate,
            baseTime,
            grid.x,
            grid.y
        ).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processWeatherResponse(response.body());
                    callback.onWeatherDataReceived();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "API 호출 실패: " + t.getMessage());
            }
        });
    }

    private void processWeatherResponse(WeatherResponse response) {
        if (response != null && response.getResponse() != null) {
            String resultCode = response.getResponse().getHeader().getResultCode();

            if ("00".equals(resultCode)) {
                WeatherResponse.Response.Body body = response.getResponse().getBody();
                if (body != null && body.getItems() != null) {
                    List<WeatherResponse.Response.Body.Items.Item> items = body.getItems().getItem();
                    Weather weather = Weather.getInstance();

                    Calendar now = Calendar.getInstance();
                    String currentHour = String.format("%02d00", now.get(Calendar.HOUR_OF_DAY));

                    for (WeatherResponse.Response.Body.Items.Item item : items) {
                        if (item.getFcstTime().equals(currentHour)) {
                            switch (item.getCategory()) {
                                case "SKY":
                                    Log.d(TAG, "SKY value: " + item.getFcstValue());
                                    weather.setSky(Integer.parseInt(item.getFcstValue()));
                                    break;
                                case "TMP":
                                    Log.d(TAG, "TMP value: " + item.getFcstValue());
                                    weather.setTemperature(Double.parseDouble(item.getFcstValue()));
                                    break;
                                case "PTY":
                                    Log.d(TAG, "PTY value: " + item.getFcstValue());
                                    weather.setPrecipitationType(Integer.parseInt(item.getFcstValue()));
                                    break;
                                case "PCP":
                                    String value = item.getFcstValue();
                                    Log.d(TAG, "PCP value: " + item.getFcstValue());
                                    if ("강수없음".equals(value)) {
                                        weather.setPrecipitation(0.0);
                                    } else {
                                        value = value.replace("mm", "");
                                        weather.setPrecipitation(Double.parseDouble(value));
                                    }
                                    break;
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "API Error: " + response.getResponse().getHeader().getResultMsg());
            }
        }
    }

}
