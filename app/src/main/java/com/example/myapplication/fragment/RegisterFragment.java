package com.example.myapplication.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.ApiUserService;
import com.example.myapplication.service.RetrofitClient;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private String tag = "RegisterFragment";

    private View root;
    private ImageView back;
    private TextInputLayout username_layout;
    private TextInputEditText username;
    private TextInputLayout password_layout;
    private TextInputEditText password;
    private TextInputLayout confirm_layout;
    private TextInputEditText confirm;
    private TextInputLayout phone_layout;
    private TextInputEditText phone;
    private Button register;
    private TextView jump_to_login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade_in));
        setExitTransition(inflater.inflateTransition(R.transition.fade_out));
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
        root = inflater.inflate(R.layout.fragment_register, container, false);

        // 没有单独extract函数initView的必要（顿悟
        back = root.findViewById(R.id.fragment_register_back);
        username_layout = root.findViewById(R.id.register_username_layout);
        username = root.findViewById(R.id.register_username);
        password_layout = root.findViewById(R.id.register_password_layout);
        password = root.findViewById(R.id.register_password);
        confirm_layout = root.findViewById(R.id.register_confirm_layout);
        confirm = root.findViewById(R.id.register_confirm);
        phone_layout = root.findViewById(R.id.register_phone_layout);
        phone = root.findViewById(R.id.register_phone);
        register = root.findViewById(R.id.register_button);
        jump_to_login = root.findViewById(R.id.jump_to_login);

        back.setOnClickListener(view -> requireActivity().finish());

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

        confirm.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                isConfirmValid();
            }
        });

        phone.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                isPhoneValid();
            }
        });

        register.setOnClickListener(view -> {
            if (isUsernameValid() && isPasswordValid()
                    && isConfirmValid() && isPhoneValid()) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String phoneStr = phone.getText().toString();
                Log.i(tag, "register process begins");
                Log.i(tag, "username: " + username + " password: *** phone: " + phoneStr);
                ApiUserService userService = RetrofitClient.getInstance().getApiUserService();
                Call<Result<String>> resultCall = userService.apiRegister(usernameStr, passwordStr, phoneStr);
                resultCall.enqueue(new Callback<Result<String>>() {
                    @Override
                    public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                int code = response.body().getCode();
                                String msg = response.body().getMsg();
                                String token;
                                if (code == ResultCode.REGISTER_SUCCESS) {
                                    Log.i(tag, "注册成功");
                                    token = response.body().getData();
                                    SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("login", true);
                                    editor.putString("token", token);
                                    editor.putString("username", usernameStr);
                                    editor.apply();
                                    jumpToProfile();
                                }
                                Log.i(tag, "response result: " + msg);
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(mContext, "返回结果为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, response.code() + "", Toast.LENGTH_SHORT).show();
                            Log.i(tag, "http status code not correct: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<String>> call, Throwable t) {
                        t.printStackTrace();
                        Log.i(tag, "register network error.");
                        Toast.makeText(mContext, "网络连接错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        jump_to_login.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activity_user_fragment_container_view, LoginFragment.class, null)
                    .commit();
        });
        return root;
    }

    private void jumpToProfile() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activity_user_fragment_container_view, UserProfileFragment.class, null)
                .commit();
    }


    private boolean isUsernameValid() {
        String text = username.getText().toString();
        if (text.isEmpty()) {
            username_layout.setError("用户名不为空");
            return false;
        }
        if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z\\d-~_.]+", text)) {
            username_layout.setError("用户名不得包含-_.~外的特殊字符");
            return false;
        }
        username_layout.setError(null);
        return true;
    }

    private boolean isPasswordValid() {
        String text = password.getText().toString();
        if (text.isEmpty()) {
            password_layout.setError("密码不为空");
            return false;
        }
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&_.]{8,}$", text)) {
            password_layout.setError("密码至少包含大小写字母和数字，且长度不少于8");
            return false;
        }
        password_layout.setError(null);
        return true;
    }

    private boolean isConfirmValid() {
        String text = confirm.getText().toString();
        if (text.isEmpty()) {
            confirm_layout.setError("确认密码不为空");
            return false;
        }
        if (!text.equals(password.getText().toString())) {
            confirm_layout.setError("两次密码输入不一致");
            return false;
        }
        confirm_layout.setError(null);
        return true;
    }

    private boolean isPhoneValid() {
        String text = phone.getText().toString();
        if (text.isEmpty()) {
            phone_layout.setError("号码不能为空");
            return false;
        }
        // 是不match！！！
        if (!Pattern.matches("^1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[235-8]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|66\\d{2})\\d{6}$", text)) {
            phone_layout.setError("号码格式错误");
            return false;
        }
        phone_layout.setError(null);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "register On Resume.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(tag, "register On Pause.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "register On Destroy.");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(tag, "register On Detach.");
    }
}
