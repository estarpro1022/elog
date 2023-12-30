package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserProfileAdapter;
import com.example.myapplication.data.UserProfileItem;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView usrIcon = view.findViewById(R.id.userIcon);
        usrIcon.setImageResource(R.drawable.ic_launcher);

        TextView usrname = view.findViewById(R.id.UserName);
        usrname.setText("username");

        ListView listView = view.findViewById(R.id.listView);

        List<UserProfileItem> items = new ArrayList<>();
        items.add(new UserProfileItem("用户名修改", R.drawable.ic_launcher));
        items.add(new UserProfileItem("密码修改", R.drawable.ic_launcher));
        items.add(new UserProfileItem("应用锁设置", R.drawable.ic_launcher));
        items.add(new UserProfileItem("登出", R.drawable.ic_launcher));

        UserProfileAdapter adapter = new UserProfileAdapter(requireContext(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
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
