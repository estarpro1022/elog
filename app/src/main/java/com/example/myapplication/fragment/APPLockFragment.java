package com.example.myapplication.fragment;////package com.example.myapplication.fragment;
////
////import android.annotation.SuppressLint;
////import android.content.SharedPreferences;
////import android.os.Bundle;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.Button;
////import android.widget.CompoundButton;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.ListView;
////import android.widget.Switch;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.fragment.app.Fragment;
////
////import com.example.myapplication.R;
////import com.example.myapplication.adapter.UserProfileAdapter;
////import com.example.myapplication.data.UserProfileItem;
////
////import java.util.ArrayList;
////import java.util.List;
////
////// HelpFragment.java
////public class APPLockFragment extends Fragment {
////    private ImageView backHelp;
////    private EditText editTextPassword;
////    private EditText editTextConfirmPassword;
////    private SharedPreferences sharedPreferences;
////    private Button buttonSave;
////    @SuppressLint("UseSwitchCompatOrMaterialCode")
////    Switch switchAppLock;
////    public APPLockFragment() {
////        // Required empty public constructor
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        View rootView = inflater.inflate(R.layout.fragment_lock_set, container, false);
////        // 在此处找到并设置容器中的返回按钮为不可见
////        ImageView back = requireActivity().findViewById(R.id.activity_user_back);
////        if (back != null) {
////            back.setVisibility(View.GONE);
////        }
////
////        initView(rootView);
////        return rootView;
////    }
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        editTextPassword = view.findViewById(R.id.editTextPassword);
////        editTextConfirmPassword  = view.findViewById(R.id.editTextConfirmPassword);
////        buttonSave = view.findViewById(R.id.buttonSave);
////        switchAppLock = view.findViewById(R.id.switchAppLock);
////        switchAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                // 处理开关状态变化的逻辑
////                if (isChecked) {
////                    // 开关打开时的处理
////                    editTextPassword.setVisibility(View.VISIBLE);
////                    editTextConfirmPassword.setVisibility(View.VISIBLE);
////                    buttonSave.setVisibility(View.VISIBLE);
////                } else {
////                    // 开关关闭时的处理
////                    editTextPassword.setVisibility(View.GONE);
////                    editTextConfirmPassword.setVisibility(View.GONE);
////                    buttonSave.setVisibility(View.GONE);
////                }
////            }
////        });
////    }
////    private void initView(View rootView) {
////        backHelp = rootView.findViewById(R.id.imageViewBackHelp);
////        backHelp.setOnClickListener(view -> navigateToUserProfile());
////    }
////
////    private void navigateToUserProfile() {
////        // 返回到 UserProfileFragment
////        getParentFragmentManager().popBackStack();
////    }
////}
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.myapplication.R;
//
//public class APPLockFragment extends Fragment {
//    private ImageView backHelp;
//    private EditText editTextPassword;
//    private EditText editTextConfirmPassword;
//    private SharedPreferences sharedPreferences;
//    private Button buttonSave;
//    private Switch switchAppLock;
//
//    public APPLockFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_lock_set, container, false);
//        // 在此处找到并设置容器中的返回按钮为不可见
//        ImageView back = requireActivity().findViewById(R.id.activity_user_back);
//        if (back != null) {
//            back.setVisibility(View.GONE);
//        }
//
//        initView(rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        editTextPassword = view.findViewById(R.id.editTextPassword);
//        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
//        buttonSave = view.findViewById(R.id.buttonSave);
//        switchAppLock = view.findViewById(R.id.switchAppLock);
//
//        sharedPreferences = requireContext().getSharedPreferences("AppLockPrefs", 0);
//
//        switchAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // 处理开关状态变化的逻辑
//                if (isChecked) {
//                    // 开关打开时的处理
//                    editTextPassword.setVisibility(View.VISIBLE);
//                    editTextConfirmPassword.setVisibility(View.VISIBLE);
//                    buttonSave.setVisibility(View.VISIBLE);
//                } else {
//                    // 开关关闭时的处理
//                    editTextPassword.setVisibility(View.GONE);
//                    editTextConfirmPassword.setVisibility(View.GONE);
//                    buttonSave.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                savePassword();
//            }
//        });
//    }
//
//    private void initView(View rootView) {
//        backHelp = rootView.findViewById(R.id.imageViewBackHelp);
//        backHelp.setOnClickListener(view -> navigateToUserProfile());
//    }
//
//    private void navigateToUserProfile() {
//        // 返回到 UserProfileFragment
//        getParentFragmentManager().popBackStack();
//    }
//
//    private void savePassword() {
//        String password = editTextPassword.getText().toString();
//        String confirmPassword = editTextConfirmPassword.getText().toString();
//
//        if (!password.equals(confirmPassword)) {
//            // 两次密码不一致，给出错误提示并清空密码
//            Toast.makeText(requireContext(), "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
//            editTextPassword.setText("");
//            editTextConfirmPassword.setText("");
//        } else {
//            // 密码一致，保存密码和设置状态到 SharedPreferences
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("appLockPassword", password);
//            editor.putBoolean("isAppLockEnabled", switchAppLock.isChecked());
//            editor.apply();
//
//            Toast.makeText(requireContext(), "密码已保存", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class APPLockFragment extends Fragment {
    private ImageView backHelp;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private SharedPreferences sharedPreferences;
    private Button buttonSave;


    public APPLockFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade_out));
        setEnterTransition(inflater.inflateTransition(R.transition.fade_in));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = requireContext().getSharedPreferences("LOCK", 0);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lock_set, container, false);
        // 在此处找到并设置容器中的返回按钮为不可见
        ImageView back = requireActivity().findViewById(R.id.activity_user_back);
        if (back != null) {
            back.setVisibility(View.GONE);
        }

        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        buttonSave = view.findViewById(R.id.buttonSave);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchAppLock = view.findViewById(R.id.switchAppLock);

        if(sharedPreferences.getBoolean("isAppLockEnabled",false)){
            switchAppLock.setChecked(true);
            editTextPassword.setVisibility(View.VISIBLE);
            editTextConfirmPassword.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
        }else{
            switchAppLock.setChecked(false);
        }

        switchAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 处理开关状态变化的逻辑
                if (isChecked) {
                    // 开关打开时的处理
                    editTextPassword.setVisibility(View.VISIBLE);
                    editTextConfirmPassword.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);
                } else {
                    // 开关关闭时的处理
                    editTextPassword.setVisibility(View.GONE);
                    editTextConfirmPassword.setVisibility(View.GONE);
                    buttonSave.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("isAppLockEnabled", false);
                    editor.apply();

                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
    }

    private void initView(View rootView) {
        backHelp = rootView.findViewById(R.id.imageViewBackHelp);
        backHelp.setOnClickListener(view -> navigateToUserProfile());

    }

    private void navigateToUserProfile() {
        // 返回到 UserProfileFragment
        getParentFragmentManager().popBackStack();
    }

    private void savePassword() {
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            // 两次密码不一致，显示错误提示框
            showAlertDialog("错误", "两次密码不一致，请重新输入");
            editTextPassword.setText("");
            editTextConfirmPassword.setText("");
        } else {
            // 密码一致，保存密码和设置状态到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("appLockPassword", password);
            editor.putBoolean("isAppLockEnabled", true);
            editor.apply();

            // 显示成功提示框
            showAlertDialog("成功", "密码已保存", (dialog, which) -> {
                // 保存密码成功后，点击确定按钮后退出当前 Fragment
                getParentFragmentManager().popBackStack();
            });
        }
    }
    private void showAlertDialog(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .show();
    }
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确定按钮点击事件
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
