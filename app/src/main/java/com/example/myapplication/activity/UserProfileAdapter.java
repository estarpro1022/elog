package com.example.myapplication.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.List;

public class UserProfileAdapter extends ArrayAdapter<UserProfileItem> {

    public UserProfileAdapter(Context context, List<UserProfileItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserProfileItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_profile, parent, false);
        }
        convertView.getLayoutParams().height = 200; // 200是像素，你可以适应自己的需要进行调整

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);

        if (item != null) {
            textViewTitle.setText(item.getTitle());
            imageViewIcon.setImageResource(item.getIconResId());
        }

        return convertView;
    }
}


