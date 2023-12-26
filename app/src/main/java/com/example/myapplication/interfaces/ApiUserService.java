package com.example.myapplication.interfaces;

import com.example.myapplication.utils.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiUserService {
    @GET("user/login")
    Call<Result<Integer>> apiLogin(@Query("username") String username,
                                   @Query("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<Result<Integer>> apiRegister(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("phone") String phone);
}
