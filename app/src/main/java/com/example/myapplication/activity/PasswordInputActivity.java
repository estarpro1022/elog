package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class PasswordInputActivity extends AppCompatActivity {

    private EditText editTextPassword;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        editTextPassword = findViewById(R.id.editTextPassword);

        // 设置数字键盘
        editTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    }

    public void onEnterClicked(View view) {
        String enteredPassword = editTextPassword.getText().toString();

        // 验证密码
        if (isPasswordCorrect(enteredPassword)) {
            // 密码正确，跳转到主界面
            preferences = getSharedPreferences("LOCK", MODE_PRIVATE);
            preferences.edit().putBoolean("isPasswordVerified", true).apply();

            Toast.makeText(this, "密码正确", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            // 密码错误，显示错误消息
            Toast.makeText(this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordCorrect(String enteredPassword) {
        preferences = getSharedPreferences("LOCK", MODE_PRIVATE);

        // 验证密码
        String savedPassword = preferences.getString("appLockPassword", "");

        return enteredPassword.equals(savedPassword);
    }

}

