package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activity.UserProfileAdapter;
import com.example.myapplication.activity.UserProfileItem;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ImageView usrIcon = findViewById(R.id.userIcon);
        usrIcon.setImageResource(R.drawable.ic_launcher);
        TextView usrname = findViewById(R.id.UserName);
        usrname.setText("username");
        ListView listView = findViewById(R.id.listView);

        List<UserProfileItem> items = new ArrayList<>();
        items.add(new UserProfileItem("用户名修改", R.drawable.ic_launcher));
        items.add(new UserProfileItem("密码修改", R.drawable.ic_launcher));
        items.add(new UserProfileItem("应用锁设置", R.drawable.ic_launcher));
        items.add(new UserProfileItem("登出", R.drawable.ic_launcher));


        UserProfileAdapter adapter = new UserProfileAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 处理列表项点击事件
            UserProfileItem selectedItem = items.get(position);
            handleListItemClick(selectedItem);
        });
    }

    private void handleListItemClick(UserProfileItem item) {
        // 根据点击的列表项进行相应的操作
        switch (item.getTitle()) {
            case "用户名展示":
                // 处理用户名展示点击事件
                break;
            case "用户名修改":
                // 处理用户名修改点击事件
                break;
            case "密码修改":
                // 处理密码修改点击事件
                break;
            case "应用锁设置":
                // 处理应用锁设置点击事件
                break;
            case "登出":
                // 处理登出点击事件
                break;
        }
    }
}
