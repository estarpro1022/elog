package com.example.myapplication.utils;

import android.util.Log;

import com.example.myapplication.interfaces.ApiWeather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService {
    private static String tag = "WeatherService";
    private static String url = "https://api.openweathermap.org/";
    private static String latitude = "32.06639";
    private static String longitude = "118.77013";
    private static String api_key = "809e1be891f1fd5f3383c1807c1b9844";
    private static String language = "zh_cn";
    private static String temperature;
    private static String weather;

    public static void apiGetTemperature() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiWeather apiWeather = retrofit.create(ApiWeather.class);
        Call<JsonObject> result = apiWeather.getWeatherInfo(latitude, longitude, api_key, language);
        System.out.println("visit weather");
        result.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.i(tag, "返回天气结果");
                    try {
                        assert response.body() != null;
                        System.out.println(response.body().toString());
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        System.out.println("visit temperature");
                        // 开尔文转为摄氏度
                        temperature = String.format("%.0f", object.getJSONObject("main").getDouble("temp") - 273.15);
                        temperature += "°C";
                        weather = object.getJSONArray("weather").getJSONObject(0).getString("description");
                        System.out.println("成功");
                        Log.i(tag, "temperature: " + temperature);
                        Log.i(tag, "weather: " + weather);
                    } catch (JSONException e) {
                        System.out.println("失败");
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.i(tag, "天气信息获取失败");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(tag, "天气状况获取失败");
            }
        });
    }

    public static String getTemperature() {
        return temperature;
    }

    public static String getWeather() {
        return weather;
    }
}
