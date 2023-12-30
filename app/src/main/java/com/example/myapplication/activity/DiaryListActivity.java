package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.adapter.DiaryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Diary;
import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.interfaces.OnItemDiaryClickListener;

import java.util.Comparator;
import java.util.List;

public class DiaryListActivity extends AppCompatActivity implements OnItemDiaryClickListener {
    private String tag = "DiaryListActivity";

    private ImageView back;
    private RecyclerView recyclerView;
    private TextView hint;
    private SearchView searchView;
    private DiaryDao diaryDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        back = findViewById(R.id.activity_diary_list_back);
        hint = findViewById(R.id.activity_diary_list_hint);
        back.setOnClickListener(view -> {
            finish();
        });
        diaryDao = DiaryDatabase.getInstance(this).getDiaryDao();
        initSearch();
    }

    private void initSearch(){
        searchView = findViewById(R.id.diary_search);
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;//这里return false不需要修改，false的作用是当你输入要搜索的文字点击搜索按钮后，手机键盘会自动消失，你把false改成true，键盘不会消失。
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {//此方法的作用是对搜索框里的文字实时监听。
                if (!TextUtils.isEmpty(newText)){

                }else{

                }
                return false;
            }
        });

    }



    /**
     * 删除日记后刷新界面
     */
    @Override
    protected void onStart() {
        super.onStart();

        List<Diary> diaries = diaryDao.queryAllDiaries();
        diaries.sort(Comparator.comparing(Diary::getDate));
        diaries.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        for (Diary diary: diaries) {
            Log.i(tag, "diary");
        }
        if (diaries.size() == 0) {
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