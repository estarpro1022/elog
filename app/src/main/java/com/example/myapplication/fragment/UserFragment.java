package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.User;
import com.example.myapplication.service.RetrofitClient;
import com.example.myapplication.utils.Result;
import com.example.myapplication.utils.ResultCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    private String tag = "UserFragment";
    private Context mContext;
    private View root;
    private Button logout;

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
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_user, container, false);
        logout = root.findViewById(R.id.fragment_user_logout);

        logout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
            Call<Result<Boolean>> resultCall = RetrofitClient.getInstance().getApiUserService().apiLogout(sharedPreferences.getString("token", ""),
                    sharedPreferences.getString("username", ""));
            resultCall.enqueue(new Callback<Result<Boolean>>() {
                @Override
                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            int code = response.body().getCode();
                            String msg = response.body().getMsg();
                            boolean result;
                            if (code == ResultCode.LOGOUT_SUCCESS) {
                                result = response.body().getData();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("login", false);
                                editor.remove("token");
                                editor.remove("username");
                                editor.apply();
                                Toast.makeText(mContext, "登出成功", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.activity_user_fragment_container_view, LoginFragment.class, null)
                                        .setReorderingAllowed(true)
                                        .commit();
                                return;
                            }
                        }
                    }
                    Toast.makeText(mContext, "登出失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                    Toast.makeText(mContext, "网络连接故障", Toast.LENGTH_SHORT).show();
                }
            });

        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}