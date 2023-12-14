package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CustomDecorator implements DayViewDecorator {

    private CalendarDay dateToDecorate;

    public CustomDecorator(CalendarDay dateToDecorate) {
        this.dateToDecorate = dateToDecorate;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(dateToDecorate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // 在这里设置你想要的日期属性，例如背景色、文本颜色等
//        view.addSpan(new ForegroundColorSpan(Color.RED)); // 设置文本颜色为红色
//        // 添加其他属性...
//        view.addSpan(Typeface.BOLD);
        view.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        // 创建一个圆形背景
        ShapeDrawable circleDrawable = new ShapeDrawable(new OvalShape());
        circleDrawable.getPaint().setColor(Color.RED);

        // 设置圆形背景
        view.setBackgroundDrawable(circleDrawable);
    }
    public void removeDecorator(DayViewFacade view) {
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
