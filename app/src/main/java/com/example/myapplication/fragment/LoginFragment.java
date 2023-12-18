package com.example.myapplication.fragment;

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
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    private String tag = "LoginFragment";
    private View root;
    private TextInputEditText username;
    private TextInputEditText password;
    private Button login;
    private TextView jump_to_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade_out));
//        setEnterTransition(inflater.inflateTransition(R.transition.fade_in));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        return root;
    }


    private void initView() {
        username = root.findViewById(R.id.register_username);
        password = root.findViewById(R.id.login_password);
        login = root.findViewById(R.id.register_button);
        login.setOnClickListener(view -> {
            Log.i(tag, "username: " + username.getText().toString());
            Log.i(tag, "password: " + password.getText().toString());
        });
        jump_to_register = root.findViewById(R.id.jump_to_register);
        jump_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_user_fragment_container_view, RegisterFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(getContext(), "I'm going to jump to register", Toast.LENGTH_SHORT).show();
            }
        });
    }


}