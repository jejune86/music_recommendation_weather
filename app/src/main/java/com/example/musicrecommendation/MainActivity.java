package com.example.musicrecommendation;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicrecommendation.data.model.Weather;
import com.example.musicrecommendation.data.model.WeatherResponse;
import com.example.musicrecommendation.service.WeatherAPIInterface;
import com.example.musicrecommendation.service.WeatherService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private WeatherService weatherService;

    // 콜백 인터페이스 추가
    interface LocationCallback {
        void onLocationReceived(Location location);
    }

    private TextView tvTemperature, tvSky, tvPrecipitationType, tvPrecipitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvSky = findViewById(R.id.tvSky);
        tvPrecipitationType = findViewById(R.id.tvPrecipitationType);
        tvPrecipitation = findViewById(R.id.tvPrecipitation);


        // 초기 날씨 데이터 표시
        Weather weather = Weather.getInstance();
        tvTemperature.setText("온도: 로딩 중...");
        tvSky.setText("하늘 상태: 로딩 중...");
        tvPrecipitationType.setText("강수 형태: 로딩 중...");
        tvPrecipitation.setText("강수량: 로딩 중...");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLastKnownLocation(new LocationCallback() {
                @Override
                public void onLocationReceived(Location location) {
                    fetchWeatherData(location);
                }
            });
        }
    }

    private void getLastKnownLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation()
            .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onLocationReceived(task.getResult());
                    }
                }
            });
    }

    private void fetchWeatherData(Location location) {
        if (location != null) {
            weatherService = new WeatherService();
            weatherService.getWeatherData(location.getLatitude(), location.getLongitude(), new WeatherService.WeatherCallback() {
                @Override
                public void onWeatherDataReceived() {
                    // UI 업데이트는 메인 스레드에서 실행해야 합니다
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Weather weather = Weather.getInstance();
                            tvTemperature.setText("온도: " + weather.getTemperature() + "°C");
                            tvSky.setText("하늘 상태: " + weather.getSky());
                            tvPrecipitationType.setText("강수 형태: " + weather.getPrecipitationType());
                            tvPrecipitation.setText("강수량: " + weather.getPrecipitation() + "mm");
                        }
                    });
                }
            });
        }
    }
}