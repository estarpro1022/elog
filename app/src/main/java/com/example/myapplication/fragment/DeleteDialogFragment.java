package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.interfaces.OnDeleteClickListener;

import java.util.function.Function;

public class DeleteDialogFragment extends DialogFragment {
    private String date;

    public void setDate(String selectedDate) {
        date = selectedDate;
    }

    public String getDate() {
        return date;
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Log.i("DeleteDialogFragment", "delete the diary confirmation.");
        builder.setTitle("确定删除这条日记吗")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDeleteClickListener.onClick(date);
                    }
                });
        return builder.create();
    }

}



//interface Callback<T> {
//    void onSuccess(T data);
//    void onErr(Throwable throwable);
//}
//
//class Http {
//    public static void doReqDiaries(String username, String token, Callback<String> callback) {
//        // HTTP
//        String result = "123";
//        callback.onSuccess(result);
//    }
//}
//
//class Act implements Callback<String> {
//    public static void onCreate() {
//        Http.doReqDiaries("", "", this);
//    }
//
//    @Override
//    public void onSuccess(String data) {
//
//    }
//
//    @Override
//    public void onErr(Throwable throwable) {
//
//    }
//}