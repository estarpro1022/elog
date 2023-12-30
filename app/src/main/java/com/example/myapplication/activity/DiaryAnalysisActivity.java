package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DiaryAnalysisActivity extends AppCompatActivity {
    private String tag = "DiaryAnalysisActivity";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_analysis);

        back = findViewById(R.id.activity_diary_analysis_back);
        back.setOnClickListener(view -> {
            finish();
        });
    }
}
