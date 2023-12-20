package com.example.myapplication.fragment;


import android.os.Bundle;
import android.transition.TransitionInflater;
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

public class RegisterFragment extends Fragment {
    private String tag = "RegisterFragment";

    private View root;
    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputEditText confirm;
    private TextInputEditText phone;
    private Button register;
    private TextView jump_to_login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade_in));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register, container, false);
        initView();
        return root;
    }

    private void initView() {
        username = root.findViewById(R.id.login_username);
        password = root.findViewById(R.id.login_password);
        confirm = root.findViewById(R.id.register_confirm);
        phone = root.findViewById(R.id.register_phone);
        register = root.findViewById(R.id.login_button);
        jump_to_login = root.findViewById(R.id.jump_to_login);
        jump_to_login.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(), "I'm back to login page.", Toast.LENGTH_SHORT).show();
        });
    }
}
