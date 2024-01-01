package com.example.myapplication.interfaces;

import com.example.myapplication.data.Diary;
import com.example.myapplication.utils.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDiaryService {
    @GET("diary/{username}")
    Call<Result<List<Diary>>> getDiaries(@Header("token") String token, @Path("username") String username);

    @POST("diary/post")
    Call<Result<Diary>> postDiary(@Header("token") String token, @Query("username") String username,
                                  @Body Diary diary);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "diary/delete", hasBody = true)
    Call<Result<Diary>> deleteDiary(@Header("token") String token,
                                    @Field("username") String username,
                                    @Field("date") String date);
}
