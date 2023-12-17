package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.myapplication.data.Diary;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.lukedeighton.wheelview.WheelView;

import androidx.appcompat.app.AppCompatActivity;

import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private WheelView wheelView;
    private ImageView user;
    private ImageView diaries;
    private ActivityMainBinding binding;
    private CalendarDay selectedDate;
    private String selectedDateString;
    private Map<String, Diary> diaryMap = new HashMap<>();
    final LinkedHashMap<String, Integer> emotionList = new LinkedHashMap<>();
    private List<Drawable> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void init(){
        imgList.add(getDrawable(R.drawable.angry));
        imgList.add(getDrawable(R.drawable.shy));
        imgList.add(getDrawable(R.drawable.hoho));
        imgList.add(getDrawable(R.drawable.good));
        imgList.add(getDrawable(R.drawable.happy));
        imgList.add(getDrawable(R.drawable.dizzy));
        imgList.add(getDrawable(R.drawable.shock));
        imgList.add(getDrawable(R.drawable.injured));
        imgList.add(getDrawable(R.drawable.decadence));
        imgList.add(getDrawable(R.drawable.sleepy));
        emotionList.put("生气", R.drawable.angry);
        emotionList.put("害羞", R.drawable.shy);
        emotionList.put("呵呵", R.drawable.hoho);
        emotionList.put("好", R.drawable.good);
        emotionList.put("非常棒", R.drawable.happy);
        emotionList.put("晕", R.drawable.dizzy);
        emotionList.put("惊吓", R.drawable.shock);
        emotionList.put("委屈", R.drawable.injured);
        emotionList.put("颓废", R.drawable.decadence);
        emotionList.put("困觉", R.drawable.sleepy);
    }

    private void initView() {
        wheelView = findViewById(R.id.wheelView);
        calendarView = findViewById(R.id.calendarView);
        user = findViewById(R.id.activity_main_user);
        diaries = findViewById(R.id.activity_main_diary);
    }

    private void initCalendarView() {
        calendarView.setTopbarVisible(true);

        // 设置 TitleFormatter 以将标题（月份）显示为中文
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return String.format("%d年%d月", day.getYear(), day.getMonth() + 1);
            }
        });

        // 设置星期的文本显示为中文
        calendarView.setWeekDayFormatter(new WeekDayFormatter() {
            @Override
            public CharSequence format(int dayOfWeek) {
                String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
                return weekDays[dayOfWeek - 1];
            }
        });

        //设置最大可选日期
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setMaximumDate(calendar).commit();

        SelectedDayDecorator selectedDayDecorator = new SelectedDayDecorator();
        calendarView.addDecorator(selectedDayDecorator);
        //TODO:当前日期之后的日期和不属于本月的日期设为不可见
        //当前日期之后的日期和不属于本月的日期设为不可见

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

    private void initUser() {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
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
                int pos = 0;
                for (int drawableResId: emotionList.values()){
                    if(drawableResId == diary.getMood()){
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