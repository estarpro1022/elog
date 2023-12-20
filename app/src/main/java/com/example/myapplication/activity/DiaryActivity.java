package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.Diary;

import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;
import com.example.myapplication.fragment.DeleteDialogFragment;
import com.example.myapplication.fragment.InfoDialogFragment;
import com.example.myapplication.interfaces.OnDeleteClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.LinkedHashMap;

public class DiaryActivity extends AppCompatActivity implements OnDeleteClickListener {

    private String tag = "DiaryActivity";
    private String selectedDate;
    private int emotionDrawable;
    private ImageView menu;
    private ConstraintLayout background;
    private ConstraintLayout textLayout;
    private TextView date;
    private ScrollView scrollView;
    private EditText content;
    private ImageButton back;
    private ImageView emotion;
    private TextView emotionText;
    private FloatingActionButton editButton;
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
        scrollView = findViewById(R.id.activity_diary_scroll_view);
        date = findViewById(R.id.elog_date);
        back = findViewById(R.id.elog_back_btn);
        content = findViewById(R.id.elog_content);
        emotion = findViewById(R.id.emotion);
        emotionText = findViewById(R.id.emotionText);
        editButton = findViewById(R.id.activity_diary_float_button);
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
        } else {
            selectedDate = intent.getStringExtra("date");
            date.setText(selectedDate);
            emotionDrawable = intent.getIntExtra("emotion", 1);
            emotion.setImageResource(emotionDrawable);
            emotionText.setText(intent.getStringExtra("emotionText"));
        }

        back.setOnClickListener(view -> finish());

        // setOnClickListener设置是没用的，click的是Scrollview的child
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // release the finger then keyboard appears.
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        if (content.requestFocus()) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_DOWN:
                        break;
                }
                return true;
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                content.requestFocus();
                editButton.setVisibility(View.INVISIBLE);
            } else {
                content.clearFocus();
                editButton.setVisibility(View.VISIBLE);
            }
        });

        editButton.setOnClickListener(view -> {
            Diary diary = new Diary(selectedDate, content.getText().toString(), emotionDrawable, emotionText.getText().toString());
            Intent intent1 = new Intent();
            intent1.putExtra("diary", diary);
            if (diaryDao.queryDiaryByDate(selectedDate) != null) {
                diaryDao.updateDiary(diary);
                Toast.makeText(DiaryActivity.this, "日记修改成功", Toast.LENGTH_SHORT).show();
            } else {
                diaryDao.insertDiary(diary);
                Toast.makeText(DiaryActivity.this, "日记保存成功", Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK, intent1);
            finish();
        });

        blank.setOnClickListener(view -> {
            if (content.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public void showPopup(View view) {
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
                    // 设置监听器，确认删除后调用DiaryActivity重写的onClick函数
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
        Toast.makeText(DiaryActivity.this, "日记已删除", Toast.LENGTH_SHORT).show();
        finish();
    }
}
