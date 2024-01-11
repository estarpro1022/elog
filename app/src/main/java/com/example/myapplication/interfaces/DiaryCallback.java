package com.example.myapplication.interfaces;

import com.example.myapplication.data.Diary;

public interface DiaryCallback<T> {
    void onSuccess(int code, String msg, T data);
    void onFailure(int code, String msg);
}
