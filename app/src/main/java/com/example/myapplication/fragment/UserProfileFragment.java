package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        usrname.setText("昵称");

        Button logout = view.findViewById(R.id.logout);
        logout.setText("退出登录");

        ListView listView = view.findViewById(R.id.listView);
        listView.setFooterDividersEnabled(true);
        listView.setHeaderDividersEnabled(true);

        List<UserProfileItem> items = new ArrayList<>();
        items.add(new UserProfileItem("修改信息", R.drawable.ic_launcher));
        items.add(new UserProfileItem("应用锁设置", R.drawable.ic_launcher));
        items.add(new UserProfileItem("帮助", R.drawable.ic_launcher));
        items.add(new UserProfileItem("版本号", R.drawable.ic_launcher));


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
            case "修改信息":
                break;
            case "应用锁设置":
                break;
            case "帮助":
                break;
            case "版本号":
                break;
        }
    }
}
