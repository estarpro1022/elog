package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.interfaces.ApiUserService;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {
    private static String tag = "UserService";
    private static String baseUrl = "http://60.204.154.139:8080/";
//    private static ApiUserService

    public static Result<Integer> login(String username, String password, Callback<Result<Integer>> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiUserService userService = retrofit.create(ApiUserService.class);
        final int[] code = new int[1];
        final int[] userid = new int[1];
        final String[] message = new String[1];
        final boolean[] isCorrect = new boolean[1];
        Call<Result<Integer>> result = userService.apiLogin(username, password);
        result.enqueue(new Callback<Result<Integer>>() {
            @Override
            public void onResponse(Call<Result<Integer>> call, Response<Result<Integer>> response) {
                Log.i(tag, "login response begins.");
                if (response.isSuccessful()) {
                    Log.i(tag, "response success.");
                    if (response.body() != null) {
                        if (code[0] == ResultCode.LOGIN_SUCCESS) {
                            userid[0] = response.body().getData();
                            isCorrect[0] = true;
                            Log.i(tag, "code: " + code[0] + " message: " + message[0]
                                    + userid[0]);
                        } else {
                            isCorrect[0] = false;
                            Log.i(tag, "code: " + code[0] + " message: " + message[0]);
                        }
                    }
                } else {
                    Log.i(tag, "状态码不正确: " + response.code());
                    code[0] = ResultCode.HTTP_STATUS_CODE_WRONG;
                    message[0] = "状态码不正确";
                    isCorrect[0] = false;
                }
            }

            @Override
            public void onFailure(Call<Result<Integer>> call, Throwable t) {
                t.printStackTrace();
                code[0] = ResultCode.INTERNET_ERROR;
                message[0] = "网络错误";
                isCorrect[0] = false;
                Log.i(tag, "传输login数据失败");
            }
        });
        if (isCorrect[0]) {
            return Result.success(code[0], message[0], userid[0]);
        } else {
            return Result.error(code[0], message[0]);
        }
    }

    public static Result<Integer> register(String username, String password, String phone) {
        return null;
    }
}
