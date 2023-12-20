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

    //删除某用户某一天的日记
    @Query("DELETE FROM DIARY WHERE DIARY_DATE = :date AND userId = :userId")
    void deleteDiaryByDate(String date, int userId);

    //查找某用户某一天的日记
    @Query("SELECT * FROM DIARY WHERE DIARY_DATE = :date AND userId = :userId")
    Diary queryDiaryByDate(String date, int userId);

    //查找某用户全部的日记
    @Query("SELECT * FROM Diary WHERE userId = :userId")
    List<Diary> getDiariesForUser(int userId);

    //删除全部日记
    @Query("DELETE FROM DIARY")
    void deleteAllDiaries();
}

