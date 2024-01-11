package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.activity.DiaryActivity;
import com.example.myapplication.data.Diary;
import com.example.myapplication.interfaces.ApiDiaryService;
import com.example.myapplication.interfaces.DiaryCallback;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryService {
    public static void postDiary(String token, String username, Diary diary) {
        ApiDiaryService apiDiaryService = RetrofitClient.getInstance().getApiDiaryService();
        Call<Result<Diary>> resultCall = apiDiaryService.postDiary(token, username, diary);
        resultCall.enqueue(new Callback<Result<Diary>>() {
            private String tag = DiaryActivity.class.getSimpleName();
            @Override
            public void onResponse(Call<Result<Diary>> call, Response<Result<Diary>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i(tag, "server saves diary");
                        int code = response.body().getCode();
                        String msg = response.body().getMsg();
                        if (code == ResultCode.POST_DIARY_SUCCESS) {
                            Diary diary1 = response.body().getData();
                            Log.i(tag, "post diary: " + diary1);
                            return;
                        }
                    }
                }
                Log.i(tag, "post diary failed.");
            }

            @Override
            public void onFailure(Call<Result<Diary>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void delete(String token, String username, String date) {
        ApiDiaryService apiDiaryService = RetrofitClient.getInstance().getApiDiaryService();
        Call<Result<Diary>> resultCall = apiDiaryService.deleteDiary(token, username, date);
        resultCall.enqueue(new Callback<Result<Diary>>() {
            private String tag = DiaryActivity.class.getSimpleName();
            @Override
            public void onResponse(Call<Result<Diary>> call, Response<Result<Diary>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i(tag, "receive delete response");
                        int code = response.body().getCode();
                        String msg = response.body().getMsg();
                        Log.i(tag, msg);
                        if (code == ResultCode.DELETE_DIARY_SUCCESS) {
                            Diary diary = response.body().getData();
                            Log.i(tag, "deleted diary: " + diary);
                            return;
                        }
                    }
                }
                Log.i(tag, "deleted failed.");
            }

            @Override
            public void onFailure(Call<Result<Diary>> call, Throwable t) {
                Log.i(tag, "network error while deleting the diary.");
                t.printStackTrace();
            }
        });
    }
}
