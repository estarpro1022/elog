package com.example.myapplication;

import java.io.Serializable;
class Diary implements Serializable{
    private String title;
    private String date;
    private String content;
    private int mood;

    public Diary(String title, String date, String content, int mood) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.mood = mood;
    }

    public String getTitle() {
        return title;
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

}
