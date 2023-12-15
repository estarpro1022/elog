package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.lukedeighton.wheelview.WheelView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int ANGRY = 0;
    public static final int SHY = 1;
    public static final int HOHO = 2;
    public static final int GOOD = 3;
    public static final int HAPPY = 4;
    public static final int DIZZY = 5;
    public static final int SHOCK = 6;
    public static final int INJURED = 7;
    public static final int DECADENCE = 8;
    public static final int SLEEPY = 9;
//    public static final int TRANSPARENT = 10;

    MaterialCalendarView calendarView;
    WheelView wheelView;
    private ActivityMainBinding binding;
    private CalendarDay selectedDate;
    private String selectedDateString;
    private Map<String, Diary> diaryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        diaryMap = new HashMap<>();
        wheelView = findViewById(R.id.wheelView);
        calendarView = findViewById(R.id.calendarView);
        //设置最大可选日期
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setMaximumDate(calendar).commit();

        SelectedDayDecorator selectedDayDecorator = new SelectedDayDecorator();
        calendarView.addDecorator(selectedDayDecorator);
        //TODO:当前日期之后的日期和不属于本月的日期设为不可见
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            // 在这里处理日期变化事件
            // date 是选中的日期
            // selected 表示日期是否被选中
            // 更新 SelectedDayDecorator 的日期
            selectedDayDecorator.setDate(date.getDate());
            // 刷新日历以应用装饰
            widget.invalidateDecorators();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(date.getYear(), date.getMonth(), date.getDay());
            selectedDate = date;
            selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar1.getTime());
            Diary diary = diaryMap.get(selectedDateString);
            if (diary != null) {
//                Toast.makeText(MainActivity.this,"111",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, 1);
            } else {// 检查 wheelView 是否为 null
                wheelView.setVisibility(View.VISIBLE);
            }
        });

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
                intent.putExtra("date", selectedDateString);
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
                CustomDecorator decorator = new CustomDecorator(selectedDate);
                decorator.setDecorated(false);
//                decorator.setColor(diary.getMood());

                decorator.setContext(this);

                calendarView.addDecorator(decorator);
                //TODO:去除selectedData日期上的装饰效果，使日期底色变成原来的颜色
            } else {
                Diary diary = (Diary) data.getSerializableExtra("diary");
                if (diaryMap.get(diary.getDate()) != null) diaryMap.remove(diary.getDate());
                diaryMap.put(diary.getDate(), diary);

                //添加装饰效果使日期底色变成红色
                CustomDecorator decorator = new CustomDecorator(selectedDate);
                decorator.setDecorated(true);
                decorator.setColor(diary.getMood());
                decorator.setContext(this);
                calendarView.addDecorator(decorator);
            }
        }
    }
}