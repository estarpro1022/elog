package com.example.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.service.RetrofitClient;
import com.example.myapplication.service.UserService;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private String tag = "LoginFragment";
    private View root;
    private TextInputLayout username_layout;
    private TextInputEditText username;
    private TextInputLayout password_layout;
    private TextInputEditText password;
    private Button login;
    private TextView jump_to_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade_out));
        setEnterTransition(inflater.inflateTransition(R.transition.fade_in));
    }

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        username_layout = root.findViewById(R.id.login_username_layout);
        username = root.findViewById(R.id.login_username);
        password_layout = root.findViewById(R.id.login_password_layout);
        password = root.findViewById(R.id.login_password);
        login = root.findViewById(R.id.login_button);
        jump_to_register = root.findViewById(R.id.jump_to_register);

        username.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                isUsernameValid();
            }
        });

        password.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                isPasswordValid();
            }
        });

        login.setOnClickListener(view -> {
            if (isUsernameValid() && isPasswordValid()) {
//                username_layout.clearFocus();
//                password_layout.clearFocus();
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                Log.i(tag, "login begins.");
                Log.i(tag, "username: " + usernameStr);
//                Log.i(tag, "password: " + passwordStr);
                Call<Result<Integer>> resultCall = RetrofitClient.getInstance().getApiUserService().apiLogin(usernameStr, passwordStr);
                resultCall.enqueue(new Callback<Result<Integer>>() {
                    @Override
                    public void onResponse(Call<Result<Integer>> call, Response<Result<Integer>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.i(tag, "response succeeds.");
                                int code = response.body().getCode();
                                String msg = response.body().getMsg();
                                int userId;
                                if (code == ResultCode.LOGIN_SUCCESS) {
                                    SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("login", true);
                                    editor.apply();
                                    // 只有此时userId才有数据
                                    userId = response.body().getData();
                                    jumpToProfile();
                                }
                                Log.i(tag, "response result: " + msg);
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i(tag, "http status code not correct: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Integer>> call, Throwable t) {
                        t.printStackTrace();
                        Log.i(tag, "login network error occurs.");
                    }
                });
            }

        });

        jump_to_register.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_user_fragment_container_view, RegisterFragment.class, null)
                    .setReorderingAllowed(true)
//                    .addToBackStack(null)
                    .commit();
        });
        return root;
    }

    private void jumpToProfile() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activity_user_fragment_container_view, UserFragment.class, null)
                .commit();
    }

    private boolean isUsernameValid() {
        String text = username.getText().toString();
        if (text.isEmpty()) {
            username_layout.setError("用户名不为空");
            return false;
        }
        // 没有验证用户名格式，直接发给服务端
        username_layout.setError(null);
        return true;
    }

    private boolean isPasswordValid() {
        String text = password.getText().toString();
        if (text.isEmpty()) {
            password_layout.setError("密码不为空");
            return false;
        }
        // 没有验证密码格式，直接发给服务端
        password_layout.setError(null);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "login On Resume.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(tag, "login On Pause.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "login On Destroy.");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(tag, "login On Detach.");
    }

}