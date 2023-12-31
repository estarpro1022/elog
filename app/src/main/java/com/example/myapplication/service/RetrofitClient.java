package com.example.myapplication.service;

import com.example.myapplication.interfaces.ApiDiaryService;
import com.example.myapplication.interfaces.ApiUserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String baseUrl = "http://60.204.154.139:8080/";
    private static RetrofitClient instance = null;
    private ApiUserService apiUserService;


    private ApiDiaryService apiDiaryService;

    public RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiUserService = retrofit.create(ApiUserService.class);
        apiDiaryService = retrofit.create(ApiDiaryService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiUserService getApiUserService() {
        return apiUserService;
    }

    public ApiDiaryService getApiDiaryService() {
        return apiDiaryService;
    }

}
