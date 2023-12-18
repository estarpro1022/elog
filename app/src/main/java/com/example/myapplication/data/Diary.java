package com.example.myapplication.data;

import java.io.Serializable;
public class Diary implements Serializable{
    private String date;
    private String content;
    private Integer mood;
    private String moodText;

    public Diary(String date, String content, int mood, String moodText) {
        this.date = date;
        this.content = content;
        this.mood = mood;
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
