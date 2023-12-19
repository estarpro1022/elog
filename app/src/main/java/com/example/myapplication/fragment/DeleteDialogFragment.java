package com.example.myapplication.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class DeleteDialogFragment extends DialogFragment {
    private String date;

    public void setDate(String selectedDate) {
        date = selectedDate;
    }

    public String getDate() {
        return date;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
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
                        onItemClickListener.onClick(date);
                    }
                });
        return builder.create();
    }

}
