package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Diary;
import com.example.myapplication.fragment.InfoDialogFragment;

public class DiaryActivity extends AppCompatActivity {
    private String selectedDate;
    private int emotionDrawable;
    private ImageView menu;
    private LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        background = findViewById(R.id.activity_detail);
        menu = findViewById(R.id.activity_diary_menu);
        TextView date = findViewById(R.id.elog_date);
        EditText content = findViewById(R.id.elog_content);
        ImageButton back = findViewById(R.id.elog_back_btn);
        ImageButton save = findViewById(R.id.elog_save);
        ImageButton delete = findViewById(R.id.elog_delete);
        ImageView emotion = findViewById(R.id.emotion);
        TextView emotionText = findViewById(R.id.emotionText);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.length() == 0){
                    Toast.makeText(DiaryActivity.this, "Please enter content", Toast.LENGTH_SHORT).show();
                }
                else {
                    Diary diary = new Diary(selectedDate, content.getText().toString(), emotionDrawable, emotionText.getText().toString());
                    Intent intent1 = new Intent();
                    intent1.putExtra("diary", diary);
                    setResult(RESULT_OK, intent1);
                    Toast.makeText(DiaryActivity.this, "日记保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.putExtra("deletedDate", selectedDate);
                setResult(RESULT_OK, intent2);
                Toast.makeText(DiaryActivity.this, "日记已删除", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void showPopup(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.overflow_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            menu.setForceShowIcon(true);
        }
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.save) {
                    Toast.makeText(DiaryActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                } else if (id == R.id.info) {
                    InfoDialogFragment fragment = new InfoDialogFragment();
                    fragment.show(getSupportFragmentManager(), "info");
                } else if (id == R.id.delete) {
                    Toast.makeText(DiaryActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        menu.show();
    }
}
