package com.example.myapplication.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Diary implements Serializable{
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "diary_date")
    private String date;
    @ColumnInfo(name = "diary_content")
    private String content;
    @ColumnInfo(name = "diary_mood")
    private int mood;
    @ColumnInfo(name = "diary_mood_text")
    private String moodText;

    public Diary(String date, String content, int mood, String moodText) {
        this.date = date;
        this.content = content;
        this.mood = mood;
        this.moodText = moodText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public void setMoodText(String moodText) {
        this.moodText = moodText;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getMood() {
        return mood;
    }

    public String getMoodText() {return moodText;}
}
