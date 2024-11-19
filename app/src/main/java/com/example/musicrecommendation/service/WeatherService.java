package com.example.musicrecommendation.service;


import android.util.Log;

import com.example.musicrecommendation.api.WeatherAPI;
import com.example.musicrecommendation.data.model.WeatherResponse;

import java.util.Calendar;

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

    public void getWeatherData(double latitude, double longitude, String apiKey) {
        GridXY gridCoords = convertToGrid(latitude, longitude);

        // 현재 날짜와 가장 가까운 30분 단위 시간을 계산
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int closestHalfHour = (minute >= 30) ? 30 : 0;
        calendar.set(Calendar.MINUTE, closestHalfHour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 날짜와 시간 설정
        String baseDate = String.format("%tY%<tm%<td", calendar);  // 예: 20231119
        String baseTime = String.format("%tH%<tM", calendar);       // 예: 1200

        // API 호출
        Call<WeatherResponse> call = weatherAPIInterface.getWeather(
                apiKey,
                "JSON", // dataType (응답자료형식)
                baseDate,
                baseTime,
                gridCoords.x, // nx (예보지점 X 좌표)
                gridCoords.y  // ny (예보지점 Y 좌표)
        );

        // 비동기 호출
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    Log.d(TAG, "Weather Data: " + weatherResponse.toString());
                } else {
                    Log.e(TAG, "Request failed");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }

    class GridXY {
        public double x;
        public double y;
    }

    private GridXY convertToGrid(double lat_X, double lng_Y) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng_Y * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        GridXY xy = new GridXY();

        xy.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        xy.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return xy;
    }
}
