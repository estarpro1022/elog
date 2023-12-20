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
    @Query("DELETE FROM DIARY WHERE DIARY_DATE = :date")
    void deleteDiaryByDate(String date);
    @Update
    void updateDiary(Diary... diaries);

    @Query("SELECT * FROM DIARY WHERE DIARY_DATE = :date")
    Diary queryDiaryByDate(String date);

    @Query("DELETE FROM DIARY")
    void deleteAllDiaries();

    @Query("SELECT * FROM Diary WHERE userId = :userId")
    List<Diary> getDiariesForUser(int userId);


}

