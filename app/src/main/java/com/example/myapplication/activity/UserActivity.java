package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.LoginFragment;
import com.example.myapplication.fragment.UserProfileFragment;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("login", false);
        if (status) {
            // 用户已登录
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_user_fragment_container_view, userProfileFragment)
                    .commit();
        } else {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_user_fragment_container_view, loginFragment)
                    .commit();
        }
    }
}