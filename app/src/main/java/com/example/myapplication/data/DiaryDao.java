package com.example.myapplication.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DiaryDao {
    @Insert
    void insertDiary(Diary... diaries);

    @Delete
    void deleteDiary(Diary... diaries);

    @Update
    void updateDiary(Diary... diaries);

    @Query("SELECT * FROM DIARY")
    List<Diary> queryAllDiaries();

    //删除某用户某一天的日记
    @Query("DELETE FROM DIARY WHERE DIARY_DATE = :date")
    void deleteDiaryByDate(String date);

    //查找某用户某一天的日记
    @Query("SELECT * FROM DIARY WHERE DIARY_DATE = :date")
    Diary queryDiaryByDate(String date);

    //删除全部日记
    @Query("DELETE FROM DIARY")
    void deleteAllDiaries();
}

