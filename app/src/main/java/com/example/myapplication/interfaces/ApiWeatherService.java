package com.example.myapplication.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeatherService {
    @GET("data/2.5/weather")
    Call<JsonObject> getWeatherInfo(@Query("lat") String lat, @Query("lon") String lon,
                                    @Query("appid") String appid, @Query("lang") String lang);
}
