package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.lukedeighton.wheelview.WheelView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    WheelView wheelView;
    private ActivityMainBinding binding;
    private CalendarDay selectedDate;

    private Map<String, Diary> diaryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
        diaryMap = new HashMap<>();
        wheelView = findViewById(R.id.wheelView);
        calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setMaximumDate(calendar).commit();
//        calendarView.state().edit().setMaximumDate(calendar.getTime()).commit();

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            // 在这里处理日期变化事件
            // date 是选中的日期
            // selected 表示日期是否被选中
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(date.getYear(), date.getMonth(), date.getDay());
            selectedDate = date;
            String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar1.getTime());
            Diary diary = diaryMap.get(Date);
            if (diary != null) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, 1);
            } else {// 检查 wheelView 是否为 null
                wheelView.setVisibility(View.VISIBLE);
            }
        });
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                // 处理日期变更的逻辑
////                Toast.makeText(MainActivity.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_LONG).show();
//                calendarView.addDecorator(new CustomDecorator(date));
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, dayOfMonth);
//                selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
//                Diary diary = diaryMap.get(selectedDate);
//                if(diary != null){
//                    Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
//                    intent.putExtra("diary", diary);
//                    startActivityForResult(intent, 1);
//                }
//               else wheelView.setVisibility(View.VISIBLE);
//
//            }
//        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        final List<Drawable> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            imgList.add(getResources().getDrawable(R.mipmap.ic_launcher));

        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                return imgList.get(position);
            }

            @Override
            public int getCount() {
                return imgList.size();
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                wheelView.setSelected(position);
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                intent.putExtra("mood", position);
                intent.putExtra("date", selectedDate);
                startActivityForResult(intent, 1);
                wheelView.setVisibility(View.GONE);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheelView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("deletedDate")) {
                diaryMap.remove(data.getStringExtra("deletedDate"));

                String deletedDate = data.getStringExtra("deletedDate");
                diaryMap.remove(deletedDate);

                // 移除相应的装饰日期
//                CalendarDay removedDay = selectedDate; // 自定义方法，将日期字符串解析为CalendarDay
                calendarView.removeDecorator(new CustomDecorator(selectedDate));
//                decoratedDates.remove(removedDay);

                // 更新装饰器
//                updateDecorators();//TODO:移除selectedDate的装饰效果
            } else {
                Diary diary = (Diary) data.getSerializableExtra("diary");
                if (diaryMap.get(diary.getDate()) != null) diaryMap.remove(diary.getDate());
                diaryMap.put(diary.getDate(), diary);
                calendarView.addDecorator(new CustomDecorator(selectedDate));
            }
        }
    }
}