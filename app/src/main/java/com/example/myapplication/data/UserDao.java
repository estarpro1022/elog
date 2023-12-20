package com.example.myapplication.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

    @Delete
    void deleteUser(User... users);

    @Update
    void updateUser(User... users);

    @Query("SELECT * FROM USER WHERE username = :username")
    User queryUserByUsername(String username);
}
