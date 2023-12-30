package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.LoginFragment;
import com.example.myapplication.fragment.UserFragment;

public class UserActivity extends AppCompatActivity {
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("login", false);
        if (status) {
            // 用户已登录
            UserFragment userFragment = new UserFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_user_fragment_container_view, userFragment)
                    .commit();
        } else {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_user_fragment_container_view, loginFragment)
                    .commit();
        }

        initView();
    }

    private void initView() {
        back = findViewById(R.id.activity_user_back);
        back.setOnClickListener(view -> finish());
    }
}