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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiaryListActivity extends AppCompatActivity implements OnItemDiaryClickListener {
    private String tag = "DiaryListActivity";

    private ImageView back;
    private RecyclerView recyclerView;
    private TextView hint;
    private SearchView searchView;
    private DiaryDao diaryDao;
    private DiaryAdapter adapter;
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
        recyclerView = findViewById(R.id.activity_diary_list_recycler_view);
        adapter = new DiaryAdapter(diaryList);
        adapter.setOnItemDiaryClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initSearch(){
        Spinner spinner = findViewById(R.id.spinner);
        //创建一个SimpleAdapter适配器
        //第一个参数：上下文，第二个参数：数据源，第三个参数：item子布局，第四、五个参数：键值对，获取item布局中的控件id
        final SimpleAdapter s_adapter = new SimpleAdapter(this, getDataList(), R.layout.item_mood, new String[]{"image"}, new int[]{R.id.spinner_img});
        //控件与适配器绑定
        spinner.setAdapter(s_adapter);
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                diaryList = i == 0 ? diaryDao.queryAllDiaries() : diaryDao.queryDiaryByMood((String) map.get("text"));
                diaryList.sort(Comparator.comparing(Diary::getDate));
                diaryList.sort((a, b) -> b.getDate().compareTo(a.getDate()));

                // 根据搜索框更新
                String query = searchView.getQuery().toString();
                if(!query.equals("")){
                    setAdapterFilter(filterByText(diaryList, query));
                }else{
                    setAdapterFilter(diaryList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        searchView = findViewById(R.id.diary_search);
        searchView.setQueryHint("搜关键字");
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 根据搜索框更新
                if(!query.equals("")){
                    setAdapterFilter(filterByText(diaryList, query));
                }else{
                    setAdapterFilter(diaryList);
                }
                searchView.clearFocus();
                return false;//这里return false不需要修改，false的作用是当你输入要搜索的文字点击搜索按钮后，手机键盘会自动消失
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //此方法的作用是对搜索框里的文字实时监听。
                if(!TextUtils.isEmpty(newText)){
                    setAdapterFilter(filterByText(diaryList, newText));
                }else{
                    setAdapterFilter(diaryList);
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
        // 按心情更新diaryList
        Spinner spinner = findViewById(R.id.spinner);
        String selected_text = (String) ((Map<String, Object>)spinner.getSelectedItem()).get("text");
        diaryList = Objects.equals(selected_text, "全部") ? diaryDao.queryAllDiaries() : diaryDao.queryDiaryByMood(selected_text);
        diaryList.sort(Comparator.comparing(Diary::getDate));
        diaryList.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        // 按搜索框更新显示内容
        if(!searchView.getQuery().toString().equals("")){
            setAdapterFilter(filterByText(diaryList, searchView.getQuery().toString()));
        }else{
            setAdapterFilter(diaryList);
        }
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

    private List<Map<String, Object>> getDataList(){
        String[] emotionTextList = {"全部", "好", "非常棒", "害羞", "呵呵", "困觉", "晕",
                "生气", "惊吓", "委屈", "颓废"};
        int[] emotionImgList = {R.drawable.multi_mood, R.drawable.good, R.drawable.happy, R.drawable.shy, R.drawable.hoho,
                R.drawable.sleepy, R.drawable.dizzy, R.drawable.angry, R.drawable.shock,
                R.drawable.injured, R.drawable.decadence};
        List<Map<String,Object>> emotionList = new ArrayList<>();
        int size = emotionImgList.length;
        for(int i = 0; i < size; i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", emotionImgList[i]);
            map.put("text", emotionTextList[i]);
            emotionList.add(map);
        }
        return emotionList;
    }

    private void setAdapterFilter(List<Diary> diaries){
        if (diaries.size() == 0) {
            hint.setVisibility(View.VISIBLE);
        }else{
            hint.setVisibility(View.INVISIBLE);

        }
        adapter.setFilter(diaries);
    }
}