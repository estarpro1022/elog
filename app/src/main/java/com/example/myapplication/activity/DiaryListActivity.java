package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.adapter.DiaryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Diary;
import com.example.myapplication.interfaces.OnItemDiaryClickListener;

public class DiaryListActivity extends AppCompatActivity implements OnItemDiaryClickListener {
    private String tag = "DiaryListActivity";

    private ImageView back;
    private RecyclerView recyclerView;
    private TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        back = findViewById(R.id.activity_diary_list_back);
        hint = findViewById(R.id.activity_diary_list_hint);
        back.setOnClickListener(view -> {
            finish();
        });
        Intent intent = getIntent();
        Diary[] diaries = (Diary[]) intent.getSerializableExtra("diaries");
        assert diaries != null;
        for (Diary diary: diaries) {
            Log.i(tag, "diary");
        }
        if (diaries.length == 0) {
            hint.setVisibility(View.VISIBLE);
        }
        recyclerView = findViewById(R.id.activity_diary_list_recycler_view);
        DiaryAdapter adapter = new DiaryAdapter(diaries);
        adapter.setOnItemDiaryClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onClick(Diary diary) {
        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("diary", diary);
        startActivity(intent);
    }
}