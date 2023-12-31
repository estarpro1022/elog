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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserProfileAdapter;
import com.example.myapplication.data.UserProfileItem;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    private static int EDITINFO = 0;
    private static int PHONELINK = 1;
    private static int HELP = 2;
    private static int VERSION = 3;


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

        List<UserProfileItem> items = new ArrayList<>();
        items.add(new UserProfileItem("修改信息", R.drawable.ic_edit_user_info));
        items.add(new UserProfileItem("应用锁设置", R.drawable.ic_phonelink_lock));
        items.add(new UserProfileItem("帮助", R.drawable.ic_help));
        items.add(new UserProfileItem("版本号", R.drawable.ic_version));

        UserProfileAdapter adapter = new UserProfileAdapter(requireContext(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // 处理列表项点击事件
            UserProfileItem selectedItem = items.get(position);
            handleListItemClick(selectedItem);

        });
        ImageView back = requireActivity().findViewById(R.id.activity_user_back);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
        }
    }

    private void handleListItemClick(UserProfileItem item) {
        // 根据点击的列表项进行相应的操作
        switch (item.getTitle()) {
            case "修改信息":
                loadFragment(EDITINFO);
                break;
            case "应用锁设置":
                loadFragment(PHONELINK);
                break;
            case "帮助":
                loadFragment(HELP);
                break;
            case "版本号":
                loadFragment(VERSION);
                break;
        }

    }
    private void loadFragment(int operator) {
        // 创建 HelpFragment 实例
        // 获取 FragmentManager
        // 开启事务
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        // 替换当前显示的 Fragment 为 HelpFragment
        if(operator == EDITINFO){
            fragmentTransaction.replace(R.id.activity_user_fragment_container_view, new EditInfoFragment());
        }else if(operator == PHONELINK){
            fragmentTransaction.replace(R.id.activity_user_fragment_container_view, new APPLockFragment());
        } else if(operator == HELP){
            fragmentTransaction.replace(R.id.activity_user_fragment_container_view, new HelpFragment());
        }else if(operator==VERSION){
            fragmentTransaction.replace(R.id.activity_user_fragment_container_view, new VersionInfoFragment());
        }

        // 添加事务到返回栈，使用户能够通过返回按钮返回上一个 Fragment
        fragmentTransaction.addToBackStack(null);

        // 提交事务
        fragmentTransaction.commit();
    }
}
