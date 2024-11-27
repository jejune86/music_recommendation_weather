package com.example.musicrecommendation.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicrecommendation.R;
import com.example.musicrecommendation.data.model.weather.Weather;
import com.example.musicrecommendation.service.SpotifyService;
import com.example.musicrecommendation.service.WeatherService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.error.SpotifyAppRemoteException;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;

import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import com.example.musicrecommendation.service.SpotifyAPIInterface;
import com.google.gson.JsonObject;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "e82fa6ae95ff44558883989407732966";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String spotifyToken;
    private SpotifyService spotifyService;



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

    @Override
    protected void onStart() {
        super.onStart();
        AuthorizationRequest.Builder builder =
        new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
    
        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();
    
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        connectSpotify();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
   
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
           AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
   
           switch (response.getType()) {
               // Response was successful and contains auth token
               case TOKEN:
                   // Handle successful response
                   Log.d("MainActivity", "Token response: " + response.getAccessToken());
                   spotifyToken = response.getAccessToken();    
                   getSpotifyRecommendation();
                   break;
   
               // Auth flow returned an error
               case ERROR:
                   // Handle error response
                   Log.d("MainActivity", "Auth error: " + response.getError());
                   break;
   
               // Most likely auth flow was cancelled
               default:
                  // Handle other cases
                  Log.d("MainActivity", "Auth flow cancelled");
            }
        }
   }


    private void connectSpotify() {
        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
            
        try {
            SpotifyAppRemote.disconnect(mSpotifyAppRemote); // 기존 연결 해제
            SpotifyAppRemote.connect(this, connectionParams,
                    new Connector.ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("MainActivity", "Connected! Yay!");
                            //connected();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("MainActivity", "Connection failed: " + throwable.getMessage());
                            if (throwable instanceof SpotifyAppRemoteException) {
                                // 5초 후 재시도
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (mSpotifyAppRemote == null) {
                                        connectSpotify();
                                    }
                                }, 5000);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("MainActivity", "Connection failed with exception: " + e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
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

    private void getSpotifyRecommendation() {
        spotifyService = new SpotifyService();
        spotifyService.getSpotifyRecommendation(spotifyToken, new SpotifyService.SpotifyCallback() {
            @Override
            public void onSpotifyRecommendationReceived() {
                // UI 업데이트는 메인 스레드에서 실행해야 합니다
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MainActivity", "Spotify 추천 음악 받아옴");
                    }
                });
            }
        });
    }
}