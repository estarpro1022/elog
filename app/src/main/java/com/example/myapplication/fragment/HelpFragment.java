package com.example.myapplication.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

// HelpFragment.java
public class HelpFragment extends Fragment {
    private ImageView backHelp;

    public HelpFragment() {
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
//        View container_help = inflater.inflate(R.layout.activity_user,container,false);
//        ImageView back = container_help.findViewById(R.id.activity_user_back);
//        back.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        // 在此处找到并设置容器中的返回按钮为不可见
        ImageView back = requireActivity().findViewById(R.id.activity_user_back);
        if (back != null) {
            back.setVisibility(View.GONE);
        }

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        backHelp = rootView.findViewById(R.id.imageViewBackHelp);
        backHelp.setOnClickListener(view -> navigateToUserProfile());
    }

    private void navigateToUserProfile() {
        // 返回到 UserProfileFragment
        getParentFragmentManager().popBackStack();
    }
}