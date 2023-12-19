package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.Diary;
import com.example.myapplication.fragment.DeleteDialogFragment;
import com.example.myapplication.fragment.InfoDialogFragment;
import com.example.myapplication.interfaces.OnDeleteClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class DiaryActivity extends AppCompatActivity implements OnDeleteClickListener {
    private String tag = "DiaryActivity";
    private String selectedDate;
    private int emotionDrawable;
    private ImageView menu;
    private ConstraintLayout background;
    private LinearLayout textLayout;
    private TextView date;
    private TextInputEditText content;
    private ImageButton back;
    private ImageView emotion;
    private TextView emotionText;
    private FloatingActionButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        background = findViewById(R.id.activity_diary_layout);
        textLayout = findViewById(R.id.activity_diary_text_layout);
        menu = findViewById(R.id.activity_diary_menu);
        date = findViewById(R.id.elog_date);
        back = findViewById(R.id.elog_back_btn);
        content = findViewById(R.id.elog_content);
        emotion = findViewById(R.id.emotion);
        emotionText = findViewById(R.id.emotionText);
        editButton = findViewById(R.id.activity_diary_float_button);

        Intent intent = getIntent();
        if(intent.hasExtra("diary")){
            Diary diary = (Diary) intent.getSerializableExtra("diary");
            selectedDate = diary.getDate();
            emotionDrawable = diary.getMood();
            emotionText.setText(diary.getMoodText());
            emotion.setImageResource(emotionDrawable);
            date.setText(selectedDate);
            content.setText(diary.getContent());
        }else{
            selectedDate = intent.getStringExtra("date");
            date.setText(selectedDate);
            emotionDrawable = intent.getIntExtra("emotion", 1);
            emotion.setImageResource(emotionDrawable);
            emotionText.setText(intent.getStringExtra("emotionText"));
        }

        back.setOnClickListener(view -> {
            finish();
        });

        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 用户输入文本时隐藏按钮
                    editButton.setVisibility(View.GONE);
                } else {
                    editButton.setVisibility(View.VISIBLE);
                }
            }
        });

        background.setOnClickListener(view -> {
            Log.i("DiaryActivity", "close the keyboard");
            content.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
//            editButton.setVisibility(View.VISIBLE);
        });

        textLayout.setOnClickListener(view -> {
            content.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
//            editButton.setVisibility(View.VISIBLE);
        });

        editButton.setOnClickListener(view -> {
            Log.i("DiaryActivity", "open the keyboard");
            content.requestFocus();
//            editButton.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT);
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
                if (id == R.id.save) {
                    if(content.length() == 0){
                        Toast.makeText(DiaryActivity.this, "Please enter content", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Diary diary = new Diary(selectedDate, content.getText().toString(), emotionDrawable, emotionText.getText().toString());
                        Intent intent1 = new Intent();
                        intent1.putExtra("diary", diary);
                        Log.i(tag, "date: " + selectedDate);
                        setResult(RESULT_OK, intent1);
                        Toast.makeText(DiaryActivity.this, "日记保存成功", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                } else if (id == R.id.info) {
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

    @Override
    public void onClick(String selectedDate) {
        Intent intent2 = new Intent();
        intent2.putExtra("deletedDate", selectedDate);
        setResult(RESULT_OK, intent2);
        Toast.makeText(DiaryActivity.this, "日记已删除", Toast.LENGTH_SHORT).show();
        finish();
        Toast.makeText(DiaryActivity.this, "删除成功", Toast.LENGTH_LONG).show();
    }
}
