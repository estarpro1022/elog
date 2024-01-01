package com.example.myapplication.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Diary implements Serializable{
//    @PrimaryKey(autoGenerate = true)
//    int id;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "diary_date")
    private String date;
    @ColumnInfo(name = "diary_content")
    private String content;
    @ColumnInfo(name = "diary_mood")
    private int mood;
    @ColumnInfo(name = "diary_mood_text")
    private String moodText;

    // TODO: 新增属性temperature和weather
    @ColumnInfo(name = "diary_temperature")
    private String temperature;

    @ColumnInfo(name = "diary_weather")
    private String weather;

    public Diary(String date, String content, int mood, String moodText, String temperature, String weather) {
        this.date = date;
        this.content = content;
        this.mood = mood;
        this.moodText = moodText;
        this.temperature = temperature;
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", mood=" + mood +
                ", moodText='" + moodText + '\'' +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMood(int mood) {
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

}
