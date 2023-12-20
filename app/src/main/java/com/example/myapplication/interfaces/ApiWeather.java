package com.example.myapplication.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeather {
    @GET("data/2.5/weather")
    Call<ResponseBody> getWeatherInfo(@Query("lat") String lat, @Query("lon") String lon,
                                      @Query("appid") String appid, @Query("lang") String lang);
}
