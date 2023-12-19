package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Diary;
import com.example.myapplication.interfaces.OnItemDiaryClickListener;

import java.util.Map;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private ImageView image;
        private TextView date;
        private TextView weather;
        private TextView temperature;
        private TextView content;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_diary_layout);
            image = itemView.findViewById(R.id.item_diary_image);
            date = itemView.findViewById(R.id.item_diary_date);
            weather = itemView.findViewById(R.id.item_diary_weather);
            temperature = itemView.findViewById(R.id.item_diary_temperature);
            content = itemView.findViewById(R.id.item_diary_content);
        }
    }

    private Diary[] diaries;

    private OnItemDiaryClickListener listener;

    public DiaryAdapter(Diary[] diaries) {
        this.diaries = diaries;
    }
    public void setOnItemDiaryClickListener(OnItemDiaryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int mood = diaries[position].getMood();
        String moodText = diaries[position].getMoodText();
        String date = diaries[position].getDate();
        String content = diaries[position].getContent();
        holder.layout.setOnClickListener(view -> {
            listener.onClick(new Diary(date, content, mood, moodText));
        });
        holder.image.setImageResource(mood);
        holder.date.setText(date);
        holder.weather.setText("阴");
        holder.temperature.setText("5°C");
        holder.content.setText(content);
    }


    @Override
    public int getItemCount() {
        return diaries.length;
    }


}
