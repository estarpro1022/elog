package com.example.myapplication;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    WheelView wheelView;
    private ActivityMainBinding binding;

    private boolean isWheelViewVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 处理日期变更的逻辑
                Toast.makeText(MainActivity.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_LONG).show();
                if(!isWheelViewVisible) {
                    wheelView.setVisibility(View.VISIBLE);
                    isWheelViewVisible = true;
                }
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        wheelView = findViewById(R.id.wheelView);

        final List<String> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) entries.add(String.valueOf(i));

        final List<Drawable> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            imgList.add(getResources().getDrawable(R.mipmap.ic_launcher));

        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                return imgList.get(position);
            }

            @Override
            public int getCount() {
                return imgList.size();
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                wheelView.setSelected(position);
                Log.e("----", "" + position);
            }
        });

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWheelViewVisible){
                    wheelView.setVisibility(View.GONE);
                    isWheelViewVisible = false;
                }
            }
        });
    }
}