package com.example.myapplication.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Diary.class, User.class},version = 1,exportSchema = false)
public abstract class DiaryDatabase extends RoomDatabase {

    public abstract DiaryDao getDiaryDao();
    public abstract UserDao getUserDao();

    private static final String DB_NAME = "diary_db";
    private static DiaryDatabase instance;

    public static synchronized DiaryDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (DiaryDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DiaryDatabase.class, DB_NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
