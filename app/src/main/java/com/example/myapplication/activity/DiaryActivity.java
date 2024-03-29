package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.Diary;

import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.fragment.DeleteDialogFragment;
import com.example.myapplication.fragment.InfoDialogFragment;
import com.example.myapplication.interfaces.ApiDiaryService;
import com.example.myapplication.interfaces.DiaryCallback;
import com.example.myapplication.interfaces.OnDeleteClickListener;
import com.example.myapplication.service.DiaryService;
import com.example.myapplication.service.RetrofitClient;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryActivity extends AppCompatActivity implements OnDeleteClickListener {

    private String tag = "DiaryActivity";
    private String selectedDate;
    private int emotionDrawable;
    private ImageView menu;
    private ConstraintLayout background;
    private ConstraintLayout textLayout;
    private TextView date;
    private ImageView back;
    private TextView temperature;
    private TextView weather;
    private ImageView emotion;
    private TextView emotionText;
    private EditText content;
    private FloatingActionButton saveButton;
    private DiaryDatabase diaryDatabase;
    private DiaryDao diaryDao;

    private View blank;

    // 告诉编译器忽略与点击触摸事件相关的无障碍辅助功能检测
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        background = findViewById(R.id.activity_diary_layout);
        textLayout = findViewById(R.id.activity_diary_text_layout);
        menu = findViewById(R.id.activity_diary_menu);
        date = findViewById(R.id.elog_date);
        back = findViewById(R.id.elog_back_btn);
        temperature = findViewById(R.id.activity_diary_temperature);
        weather = findViewById(R.id.activity_diary_weather);
        content = findViewById(R.id.elog_content);
        emotion = findViewById(R.id.emotion);
        emotionText = findViewById(R.id.emotionText);
        saveButton = findViewById(R.id.activity_diary_float_button);
        blank = findViewById(R.id.activity_diary_blank);

        diaryDatabase = DiaryDatabase.getInstance(this);
        diaryDao = diaryDatabase.getDiaryDao();

        Intent intent = getIntent();
        if (intent.hasExtra("diary")) {
            Diary diary = (Diary) intent.getSerializableExtra("diary");
            selectedDate = diary.getDate();
            emotionDrawable = diary.getMood();
            emotionText.setText(diary.getMoodText());
            emotion.setImageResource(emotionDrawable);
            date.setText(selectedDate);
            content.setText(diary.getContent());
            temperature.setText(diary.getTemperature());
            weather.setText(diary.getWeather());
        } else {
            selectedDate = intent.getStringExtra("date");
            date.setText(selectedDate);
            temperature.setText(intent.getStringExtra("temperature"));
            weather.setText(intent.getStringExtra("weather"));
            emotionDrawable = intent.getIntExtra("emotion", 1);
            emotion.setImageResource(emotionDrawable);
            emotionText.setText(intent.getStringExtra("emotionText"));
        }

        back.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                content.requestFocus();
                saveButton.setVisibility(View.INVISIBLE);
            } else {
                content.clearFocus();
                saveButton.setVisibility(View.VISIBLE);
            }
        });

        saveButton.setOnClickListener(view -> {
            saveDiary();
        });

        blank.setOnClickListener(view -> {
            if (content.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Diary diary = diaryDao.queryDiaryByDate(selectedDate);
                if (diary != null) {
                    if (diary.getContent().equals(content.getText().toString()) && diary.getMood() == emotionDrawable) {
                        finish();
                    } else {
                        popDialog();
                    }
                } else {
                    if (content.getText().toString().isEmpty()) {
                        finish();
                    } else {
                        popDialog();
                    }
                }
            }
        });

    }

    public void showPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.overflow_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 弹出菜单强制显示图案
            menu.setForceShowIcon(true);
        }
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.info) {
                    String text = content.getText().toString();
                    String emotionTextString = emotionText.getText().toString();
                    InfoDialogFragment fragment = new InfoDialogFragment("字数：" + text.length() + "\n心情：" + emotionTextString);
                    fragment.show(getSupportFragmentManager(), "info");
                } else if (id == R.id.delete) {
                    DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
                    dialogFragment.setDate(selectedDate);
                    // 设置监听器，当用户点击删除选项后调用DiaryActivity重写的onClick函数
                    dialogFragment.setOnDeleteClickListener(DiaryActivity.this);
                    dialogFragment.show(getSupportFragmentManager(), "delete");
                }
                return true;
            }
        });
        menu.show();
    }

    public void changeImage(View view) {
        final LinkedHashMap<String, Integer> emotionList = new LinkedHashMap<>();
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

        String key = "";
        for (int i = 0; i < 10; i++) {
            key = (String) emotionList.keySet().toArray()[i];
            if (emotionDrawable == emotionList.get(key)) {
                if (i == 9) {
                    key = (String) emotionList.keySet().toArray()[0];
                } else {
                    key = (String) emotionList.keySet().toArray()[i + 1];

                }
                emotionDrawable = emotionList.get(key);
                break;
            }

        }
        emotionText.setText(key);
        emotion.setImageResource(emotionDrawable);

    }

    @Override
    public void onClick(String selectedDate) {
        Intent intent2 = new Intent();
        intent2.putExtra("deletedDate", selectedDate);
        setResult(RESULT_OK, intent2);
        diaryDao.deleteDiaryByDate(selectedDate);

        // delete diary from web server
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        DiaryService.delete(sp.getString("token", ""),
                sp.getString("username", ""),
                selectedDate);
        finish();
    }

    private void saveDiary() {
        Diary result = diaryDao.queryDiaryByDate(selectedDate);
        Intent intent1 = new Intent();
        Diary diary;
        if (result != null) {
            diary = new Diary(selectedDate, content.getText().toString(), emotionDrawable, emotionText.getText().toString(), result.getTemperature(), result.getWeather());
            intent1.putExtra("diary", diary);
            diaryDao.updateDiary(diary);
        } else {
            diary = new Diary(selectedDate, content.getText().toString(), emotionDrawable, emotionText.getText().toString(), getIntent().getStringExtra("temperature"), getIntent().getStringExtra("weather"));
            intent1.putExtra("diary", diary);
            diaryDao.insertDiary(diary);
        }
        Log.i(tag, "local diary stored successfully.");

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        DiaryService.postDiary(sp.getString("token", ""),
                sp.getString("username", ""), diary);
        setResult(RESULT_OK, intent1);
        finish();
    }

    private void popDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this);
        builder.setTitle("提示")
                .setMessage("是否需要保存更改")
                .setNegativeButton("不保存", (dialogInterface, i) -> finish())
                .setPositiveButton("保存", (dialogInterface, i) -> saveDiary());
        // 不要老是忘记show
        builder.create().show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(tag, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(tag, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "onStop.");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(tag, "onDestroy");
    }

}
