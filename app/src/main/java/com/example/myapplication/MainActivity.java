package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.activity.DiaryActivity;
import com.example.myapplication.activity.DiaryListActivity;
import com.example.myapplication.activity.UserActivity;
import com.example.myapplication.data.Diary;
import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.decorator.CustomDecorator;
import com.example.myapplication.decorator.SelectedDayDecorator;
import com.lukedeighton.wheelview.WheelView;

import androidx.appcompat.app.AppCompatActivity;

import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private MaterialCalendarView calendarView;
    private WheelView wheelView;
    private ImageView user;
    private ImageView diaries;
    private ActivityMainBinding binding;
    private CalendarDay selectedDate;
    private String selectedDateString;
    SelectedDayDecorator selectedDayDecorator = new SelectedDayDecorator();

    private Map<String, Diary> diaryMap = new HashMap<>();
    final LinkedHashMap<String, Integer> emotionList = new LinkedHashMap<>();
    private List<Drawable> imgList = new ArrayList<>();
    private DiaryDao diaryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化数据库
        diaryDao = DiaryDatabase.getInstance(this).getDiaryDao();

        // 初始化View组件
        initView();

        // 初始化心情列表
        init();

        // 初始化MaterialCalendarView组件
        initCalendarView();

        // 初始化WheelView组件
        initWheelView();

        // 初始化用户的ImageView组件
        initUser();

        // 初始化日记总览的ImageView组件
        initDiaries();


    }

    @Override
    protected void onStart() {
        super.onStart();
        decorateCalendarView();
    }

    private void decorateCalendarView() {
        calendarView.removeDecorators();
        // 日历字体样式
        calendarView.addDecorator(selectedDayDecorator);

        // 日历背景样式
        List<Diary> diaryList = diaryDao.queryAllDiaries();
        for (Diary diary: diaryList) {
            int year = Integer.parseInt(diary.getDate().substring(0, 4));
            // month: 0-11
            int month = Integer.parseInt(diary.getDate().substring(5, 7)) - 1;
            int day = Integer.parseInt(diary.getDate().substring(8, 10));
            Log.i(tag, "year: " + year + " month: " + month + " day: " + day);
            CalendarDay calendarDay = CalendarDay.from(year, month, day);
            CustomDecorator decorator = new CustomDecorator(calendarDay);
            decorator.setDecorated(true);
            int pos = 0;
            for (int drawableResId : emotionList.values()) {
                if (drawableResId == diary.getMood()) {
                    Log.i(tag, "diary mood: " + diary.getMood());
                    decorator.setColor(pos);
                    break;
                }
                pos++;
            }
            decorator.setContext(this);
            calendarView.addDecorator(decorator);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void init(){
        imgList.add(getDrawable(R.drawable.good));
        imgList.add(getDrawable(R.drawable.happy));
        imgList.add(getDrawable(R.drawable.shy));
        imgList.add(getDrawable(R.drawable.hoho));
        imgList.add(getDrawable(R.drawable.sleepy));
        imgList.add(getDrawable(R.drawable.dizzy));
        imgList.add(getDrawable(R.drawable.angry));
        imgList.add(getDrawable(R.drawable.shock));
        imgList.add(getDrawable(R.drawable.injured));
        imgList.add(getDrawable(R.drawable.decadence));
        emotionList.put("好", R.drawable.good);
        emotionList.put("非常棒", R.drawable.happy);
        emotionList.put("害羞", R.drawable.shy);
        emotionList.put("呵呵", R.drawable.hoho);
        emotionList.put("困觉", R.drawable.sleepy);
        emotionList.put("晕", R.drawable.dizzy);
        emotionList.put("生气", R.drawable.angry);
        emotionList.put("惊吓", R.drawable.shock);
        emotionList.put("委屈", R.drawable.injured);
        emotionList.put("颓废", R.drawable.decadence);
    }

    private void initView() {
        wheelView = findViewById(R.id.wheelView);
        calendarView = findViewById(R.id.calendarView);
        user = findViewById(R.id.activity_main_user);
        diaries = findViewById(R.id.activity_main_diary);
    }

    private void initCalendarView() {
        // 渲染已写日记
        decorateCalendarView();

        calendarView.setTopbarVisible(true);

        // 设置 TitleFormatter 以将标题（月份）显示为中文
        calendarView.setTitleFormatter(day -> String.format("%d年%d月", day.getYear(), day.getMonth() + 1));

        // 设置星期的文本显示为中文
        calendarView.setWeekDayFormatter(dayOfWeek -> {
            String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
            return weekDays[dayOfWeek - 1];
        });

        //设置最大可选日期
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setMaximumDate(calendar).commit();
//        SelectedDayDecorator selectedDayDecorator = new SelectedDayDecorator();
        calendarView.addDecorator(selectedDayDecorator);

        //TODO:当前日期之后的日期和不属于本月的日期设为不可见
        //当前日期之后的日期和不属于本月的日期设为不可见

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            // 在这里处理日期变化事件
            // date 是选中的日期
            // selected 表示日期是否被选中
            // 更新 SelectedDayDecorator 的日期

            selectedDayDecorator.setDecorateSelected(true);

            selectedDayDecorator.setDate(date.getDate());
            // 刷新日历以应用装饰
            widget.invalidateDecorators();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(date.getYear(), date.getMonth(), date.getDay());
            selectedDate = date;
            selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar1.getTime());
            Diary diary = diaryDao.queryDiaryByDate(selectedDateString);
//            Diary diary = diaryMap.get(selectedDateString);
            if (diary != null) {
//                Toast.makeText(MainActivity.this,"111",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, 1);
            } else {// 检查 wheelView 是否为 null
                wheelView.post(new Runnable() {
                    @Override
                    public void run() {
                        int nextPosition = wheelView.getSelectedPosition() + 1;
                        if(nextPosition >= wheelView.getWheelItemCount()){
                            nextPosition = 0;
                        }
                        wheelView.setSelected(nextPosition);
                    }
                });

                wheelView.setVisibility(View.VISIBLE);

//                wheelView.setSelectionAngle(wheelView.getSelectionAngle()+36);
            }
        });
    }

    private void initWheelView() {
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
                String key = (String) emotionList.keySet().toArray()[position];
                intent.putExtra("emotionText", key);
                intent.putExtra("emotion", emotionList.get(key));
                intent.putExtra("date", selectedDateString);
                startActivityForResult(intent, 1);
                calendarView.clearSelection();
                wheelView.setVisibility(View.INVISIBLE);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheelView.setVisibility(View.INVISIBLE);
                calendarView.clearSelection();
                selectedDayDecorator.setDecorateSelected(false);
                calendarView.addDecorator(selectedDayDecorator);
            }
        });
    }

    private void initUser() {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDiaries() {
        diaries.setOnClickListener(view -> {
            // 发送日记列表
            // Diary类是序列化的，所以直接传就可以
            Intent intent = new Intent(this, DiaryListActivity.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("deletedDate")) {
                // TODO: 应将日记保存在数据库中，并且删除日记该直接从数据库删除
//                diaryMap.remove(data.getStringExtra("deletedDate"));
                CustomDecorator decorator = new CustomDecorator(selectedDate);
                decorator.setDecorated(false);
                decorator.setContext(this);
                calendarView.addDecorator(decorator);
                //TODO:去除selectedData日期上的装饰效果，使日期底色变成原来的颜色
            } else {
                Diary diary = (Diary) data.getSerializableExtra("diary");
//                if (diaryMap.get(diary.getDate()) != null) diaryMap.remove(diary.getDate());
//                diaryMap.put(diary.getDate(), diary);
                //添加装饰效果使日期底色变成红色
                Log.i(tag, "custom date: " + selectedDate);
                CustomDecorator decorator = new CustomDecorator(selectedDate);
                decorator.setDecorated(true);
                int pos = 0;
                for (int drawableResId : emotionList.values()) {
                    if (drawableResId == diary.getMood()) {
                        decorator.setColor(pos);
                        break;
                    }
                    pos++;
                }
                decorator.setContext(this);
                calendarView.addDecorator(decorator);
            }
        }
    }
}