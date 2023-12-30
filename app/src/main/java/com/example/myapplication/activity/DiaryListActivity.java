package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.adapter.DiaryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Diary;
import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.interfaces.OnItemDiaryClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiaryListActivity extends AppCompatActivity implements OnItemDiaryClickListener {
    private String tag = "DiaryListActivity";

    private ImageView back;
    private RecyclerView recyclerView;
    private TextView hint;
    private SearchView searchView;
    private DiaryDao diaryDao;
    private DiaryAdapter adapter;
    private String[] emotionTextList = {"心情", "好", "非常棒", "害羞", "呵呵", "困觉", "晕",
            "生气", "惊吓", "委屈", "颓废"};
    private List<Diary> diaryList;

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
        Spinner spinner = findViewById(R.id.spinner);
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this, R.layout.item_select, emotionTextList);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //设置下拉框的数组适配器
        spinner.setAdapter(starAdapter);
        //设置下拉框默认的显示第一项
        spinner.setSelection(0);
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diaryList = i == 0 ? diaryDao.queryAllDiaries() : diaryDao.queryDiaryByMood(emotionTextList[i]);
                diaryList.sort(Comparator.comparing(Diary::getDate));
                diaryList.sort((a, b) -> b.getDate().compareTo(a.getDate()));

                // 根据搜索框更新
                String query = searchView.getQuery().toString();
                if(!query.equals("")){
                    adapter.setFilter(filterByText(diaryList, query));
                }else{
                    adapter.setFilter(diaryList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        searchView = findViewById(R.id.diary_search);
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setFilter(filterByText(diaryList, query));
                searchView.clearFocus();
                return false;//这里return false不需要修改，false的作用是当你输入要搜索的文字点击搜索按钮后，手机键盘会自动消失，你把false改成true，键盘不会消失。
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //此方法的作用是对搜索框里的文字实时监听。
                if(!TextUtils.isEmpty(newText)){
                    adapter.setFilter(filterByText(diaryList, newText));
                }else{
                    adapter.setFilter(diaryList);
                }
                return true;
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
        adapter = new DiaryAdapter(diaries);
        adapter.setOnItemDiaryClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        diaryList = diaries;
    }

    @Override
    public void onClick(Diary diary) {
        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("diary", diary);
        startActivity(intent);
    }

    private List<Diary> filterByText(List<Diary> diaryList, String text){
        List<Diary> filterDiary = new ArrayList<>();

        for (Diary diary:diaryList){
            if (diary.getContent().contains(text))
                filterDiary.add(diary);
        }
        return filterDiary;
    }

    private List<Diary> filterByMood(List<Diary> diaryList, String text){
        List<Diary> filterDiary = new ArrayList<>();

        for (Diary diary:diaryList){
            if (diary.getMoodText().contains(text))
                filterDiary.add(diary);
        }
        return filterDiary;
    }
}