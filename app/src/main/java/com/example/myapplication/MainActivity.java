package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.activity.DiaryActivity;
import com.example.myapplication.activity.DiaryListActivity;
import com.example.myapplication.activity.PasswordInputActivity;
import com.example.myapplication.activity.UserActivity;
import com.example.myapplication.data.Diary;
import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.decorator.CustomDecorator;
import com.example.myapplication.decorator.SelectedDayDecorator;
import com.example.myapplication.utils.Calculate;
import com.example.myapplication.service.WeatherService;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private MaterialCalendarView calendarView;
    private WheelView wheelView;
    private ImageView user;
    private ImageView diaries;
    private CardView cardView;
    private ImageView llmButton;
    private View popupView;
    private PopupWindow popupWindow;
    private RequestListener<GifDrawable> animationListener;
    private ActivityMainBinding binding;
    private CalendarDay selectedDate;
    private String selectedDateString;
    SelectedDayDecorator selectedDayDecorator = new SelectedDayDecorator();
    final LinkedHashMap<String, Integer> emotionList = new LinkedHashMap<>();
    private final List<Drawable> imgList = new ArrayList<>();
    private DiaryDao diaryDao;

    private SharedPreferences preferences;
    private boolean fromDesktop = true;

    private boolean isPasswordVerified() {
        // 在这里添加检查密码是否已验证的逻辑
        // 可以使用 SharedPreferences 或其他方式保存密码验证状态
        // 这里只是一个示例，实际中需要根据你的需求进行更改
        preferences = getSharedPreferences("LOCK", MODE_PRIVATE);
        return preferences.getBoolean("isPasswordVerified", false);
    }

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

        // 初始化展示人格的FloatingActionButton组件
        initAnimation();
        initLLM();

        WeatherService.apiGetTemperature();
        Log.i(tag, "get temperature: " + WeatherService.getTemperature());
    }

    public void setFromDesktop(boolean fromDesktop) {
        this.fromDesktop = fromDesktop;
    }

    @Override
    protected void onStart() {
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        if (preferences.getBoolean("login", false)) {
            if (fromDesktop) {
                preferences = getSharedPreferences("LOCK", MODE_PRIVATE);
                if (preferences.getBoolean("isAppLockEnabled", false)) {
                    Toast.makeText(this, "设置了密码", Toast.LENGTH_SHORT).show();
                    // 检查是否已通过密码验证
                    if (!isPasswordVerified()) {
                        // 未通过密码验证，启动密码输入界面
                        Intent intent = new Intent(this, PasswordInputActivity.class);
                        startActivity(intent);
                        finish();  // 关闭 MainActivity
                    }
                    preferences = getSharedPreferences("LOCK", MODE_PRIVATE);
                    preferences.edit().putBoolean("isPasswordVerified", false).apply();
                }
            }
        }

        Log.i(tag, "onStart method.");
        super.onStart();
        decorateCalendarView();
        fromDesktop = true;

    }

    private void initView() {
        wheelView = findViewById(R.id.wheelView);
        calendarView = findViewById(R.id.calendarView);
        user = findViewById(R.id.activity_main_user);
        diaries = findViewById(R.id.activity_main_diary);
        llmButton = findViewById(R.id.llm_button);
        cardView = findViewById(R.id.card_view);

        user.setOnClickListener(view -> {
            fromDesktop = false;
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        });

        diaries.setOnClickListener(view -> {
            fromDesktop = false;
            startActivity(new Intent(this, DiaryListActivity.class));
        });

        binding.getRoot().setOnClickListener(view -> {
            wheelView.setVisibility(View.INVISIBLE);
            // 神奇的是，点击空白处，白色的背景缓缓消失
            calendarView.clearSelection();
            // 清除文字样式
            selectedDayDecorator.setDecorateSelected(false);
            calendarView.invalidateDecorators();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {
        int[] images = {R.drawable.good, R.drawable.happy, R.drawable.shy, R.drawable.hoho,
                R.drawable.sleepy, R.drawable.dizzy, R.drawable.angry, R.drawable.shock,
                R.drawable.injured, R.drawable.decadence};
        String[] emotionTextList = {"好", "非常棒", "害羞", "呵呵", "困觉", "晕",
                "生气", "惊吓", "委屈", "颓废"};
        for (int image : images) {
            imgList.add(getDrawable(image));
        }
        for (int i = 0; i < images.length; i++) {
            emotionList.put(emotionTextList[i], images[i]);
        }
    }

    private void initCalendarView() {
        // 渲染已写日记
        decorateCalendarView();

        calendarView.setTopbarVisible(true);
        calendarView.setTitleFormatter(day -> String.format("%d年%d月", day.getYear(), day.getMonth() + 1));
        calendarView.setWeekDayFormatter(dayOfWeek -> {
            String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
            return weekDays[dayOfWeek - 1];
        });

        //设置最大可选日期
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setMaximumDate(calendar).commit();

        // 设置文字装饰器
        calendarView.addDecorator(selectedDayDecorator);

        // 处理日期变化事件
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDayDecorator.setDecorateSelected(true);
            selectedDayDecorator.setDate(date.getDate());
            // 刷新日历以应用装饰
            widget.invalidateDecorators();

            // 选中日期
            selectedDate = date;

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(date.getYear(), date.getMonth(), date.getDay());
            selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar1.getTime());
            Diary diary = diaryDao.queryDiaryByDate(selectedDateString);
            if (diary != null) {
                // fix: 当用户先点击未写日记，再点击已写日记时，需要让轮盘不可见
                wheelView.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, 1);
            } else {
                // 更改UI为顺时针旋转，确保Runnable代码在主线程上运行
                wheelView.post(() -> {
                    int nextPosition = wheelView.getSelectedPosition() - 1;
                    if (nextPosition < 0) {
                        nextPosition = (int) wheelView.getWheelItemCount() - 1;
                    }
                    wheelView.setSelected(nextPosition);
                });
                wheelView.setVisibility(View.VISIBLE);
            }
        });
    }

    // ACTION_MOVE的距离小于MAX_DISTANCE_UP就判定为事件为click
    private static final int MAX_DISTANCE_DP = 5;
    private float pressX;
    private float pressY;
    private boolean isRotating;

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

        // 判断用户是否在转动轮盘，若是则不跳转写日记界面
        wheelView.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(tag, "wheelView click down.");
                    isRotating = false;
                    pressX = motionEvent.getX();
                    pressY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(tag, "wheelview rotate around.");
                    isRotating = true;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(tag, "wheelView click finish.");
                    float distancePixel = Calculate.distance(pressX, pressY, motionEvent.getX(), motionEvent.getY());
                    float distanceDp = Calculate.pixelToDp(distancePixel, getResources().getDisplayMetrics().density);
                    Log.i(tag, "distancePixel: " + distancePixel + " distanceDp: " + distanceDp);
                    if (distanceDp < MAX_DISTANCE_DP)
                        isRotating = false;
                    break;
            }
            // touch事件为false，否则不会继续触发WheelItemClickListener
            return false;
        });

        // 选择心情，跳转界面
        wheelView.setOnWheelItemClickListener((parent, position, isSelected) -> {
            if (isRotating) {
                Log.i(tag, "用户在转动轮盘，不跳转");
                return;
            }
            wheelView.setSelected(position);
            Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
            String key = (String) emotionList.keySet().toArray()[position];
            intent.putExtra("emotionText", key);
            intent.putExtra("emotion", emotionList.get(key));
            intent.putExtra("date", selectedDateString);
            intent.putExtra("temperature", WeatherService.getTemperature());
            intent.putExtra("weather", WeatherService.getWeather());
            startActivityForResult(intent, 1);
            calendarView.clearSelection();
            // 清除文字样式
            selectedDayDecorator.setDecorateSelected(false);
            calendarView.invalidateDecorators();
        });
    }

    private void initUser() {
        user.setOnClickListener(view -> {
            fromDesktop = false;
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        });

    }

    private void initDiaries() {
        diaries.setOnClickListener(view -> {
            fromDesktop = false;
            Intent intent = new Intent(this, DiaryListActivity.class);
            startActivity(intent);
            wheelView.setVisibility(View.INVISIBLE);
        });
    }

    private void initAnimation() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // 将layout布局转为View对象
        popupView = inflater.inflate(R.layout.cardview_layout, null);
        // 将popupView作为内容，大小自适应
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.CardViewAnimation);

        popupView.findViewById(R.id.card_view_background).setOnClickListener(view -> {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        ImageView animal = popupView.findViewById(R.id.card_view_icon);

        animationListener = new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                // 在gif动画加载完成后设置点击事件监听器
                resource.setLoopCount(1);
                animal.setOnClickListener(view -> {
                    // 播放gif动画
                    resource.start();
                    // 监听动画播放完成事件
                    // 监听动画播放完成事件
                    resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            resource.stop();
                        }
                    });
                });
                return false;
            }
        };

    }

    private void initLLM() {
        String[] names = {"无语羊驼", "活力小狗", "悲伤青蛙", "困困考拉", "爆炸河豚", "摆烂乌龟"};
        String[] descriptions = {"呸呸呸！让口水飞", "汪汪汪，快乐无限！", "在湿漉漉的荷叶上思考人生",
                "呼~呼~呼", "毁灭吧！海底世界", "懒懒散散，悠哉游哉"};
        int[] animals = {R.raw.alpaca, R.raw.dog, R.raw.frog,
                R.raw.koala, R.raw.puffer, R.raw.turtle};
        String name = "无语羊驼";
        String description = "——呸呸呸！让口水飞";
        String llm = "人生不是一场竞赛，有时候放慢脚步，适当休息，反而能够更好地迎接挑战和充实自己";

        // 提前加载
        Random random = new Random();
        int number = random.nextInt(6);
        int[] animalNumber = new int[]{number};
        ((TextView) popupView.findViewById(R.id.card_view_name)).setText(names[number]);
        ((TextView) popupView.findViewById(R.id.card_view_description)).setText("——" + descriptions[number]);
        ((TextView) popupView.findViewById(R.id.card_view_llm)).setText(llm);

        // 设置imagebutton
        llmButton.setImageResource(animals[number]);
        llmButton.setElevation(8); // 设置阴影的高度
        llmButton.setTranslationZ(4); // 设置阴影的偏移量
        llmButton.setAlpha(0.8f);

        llmButton.setOnClickListener(view -> {
            ImageView animal = popupView.findViewById(R.id.card_view_icon);
            Glide.with(getApplicationContext())
                    .asGif()
                    .load(animals[animalNumber[0]])
                    .listener(animationListener)
                    .into(animal);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }

    private void decorateCalendarView() {
        // 删除日记后，日历背景样式需要去除
        calendarView.removeDecorators();

        // 日历字体样式
        calendarView.addDecorator(selectedDayDecorator);

        // 日历背景样式
        List<Diary> diaryList = diaryDao.queryAllDiaries();
        for (Diary diary : diaryList) {
            int year = Integer.parseInt(diary.getDate().substring(0, 4));
            // month下表: 0-11
            int month = Integer.parseInt(diary.getDate().substring(5, 7)) - 1;
            int day = Integer.parseInt(diary.getDate().substring(8, 10));
            CalendarDay calendarDay = CalendarDay.from(year, month, day);
            decorateBackground(diary, calendarDay);
        }
    }

    private void decorateBackground(Diary diary, CalendarDay calendarDay) {
        CustomDecorator decorator = new CustomDecorator(calendarDay);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("deletedDate")) {
                Log.i(tag, "deal with deleted diary.");
                calendarView.clearSelection();
            } else {
                Diary diary = (Diary) data.getSerializableExtra("diary");
                Log.i(tag, "custom date: " + selectedDate);
                decorateBackground(diary, selectedDate);
            }
        }
    }
}