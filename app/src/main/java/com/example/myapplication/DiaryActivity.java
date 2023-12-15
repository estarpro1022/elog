package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity{
    private String selectedDate;
    private int emotionDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

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
}
