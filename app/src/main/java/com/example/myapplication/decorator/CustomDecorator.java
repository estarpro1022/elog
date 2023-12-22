package com.example.myapplication.decorator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CustomDecorator implements DayViewDecorator {
    public static final int GOOD = 0;
    public static final int HAPPY = 1;
    public static final int SHY = 2;
    public static final int HOHO = 3;
    public static final int SLEEPY = 4;
    public static final int DIZZY = 5;
    public static final int ANGRY = 6;
    public static final int SHOCK = 7;
    public static final int INJURED = 8;
    public static final int DECADENCE = 9;

    private CalendarDay dateToDecorate;
    private int color;
    private boolean isDecorated;
    public CustomDecorator(CalendarDay dateToDecorate) {
        this.dateToDecorate = dateToDecorate;
        this.isDecorated = false;
    }
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(dateToDecorate);
    }
    @Override
    public void decorate(DayViewFacade view) {

        if (isDecorated) {
            // 在这里设置你想要的日期属性，例如背景色、文本颜色等
            // TODO: 设置日期底色为红色
            // 创建一个圆形背景
            ShapeDrawable circleDrawable = new ShapeDrawable(new OvalShape());
            int backgroundColor = 0;

            switch (color){
                case ANGRY:
                    backgroundColor = ContextCompat.getColor(context, R.color.angry);
                    break;
                case SHY:
                    backgroundColor = ContextCompat.getColor(context, R.color.shy);
                    break;
                case HOHO:
                    backgroundColor = ContextCompat.getColor(context, R.color.hoho);
                    break;
                case GOOD:
                    backgroundColor = ContextCompat.getColor(context, R.color.good);
                    break;
                case HAPPY:
                    backgroundColor = ContextCompat.getColor(context, R.color.happy);
                    break;
                case DIZZY:
                    backgroundColor = ContextCompat.getColor(context, R.color.dizzy);
                    break;
                case SHOCK:
                    backgroundColor = ContextCompat.getColor(context, R.color.shock);
                    break;
                case INJURED:
                    backgroundColor = ContextCompat.getColor(context, R.color.injured);
                    break;
                case DECADENCE:
                    backgroundColor = ContextCompat.getColor(context, R.color.decadence);
                    break;
                case SLEEPY:
                    backgroundColor = ContextCompat.getColor(context, R.color.sleepy);
                    break;
                default:
                    break;
            }
            circleDrawable.getPaint().setColor(backgroundColor);
            // 设置圆形背景
            view.setBackgroundDrawable(circleDrawable);
        } else {
            // TODO: 设置日期底色为原来的颜色
            view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    // 新添加的方法，用于控制是否已装饰
    public void setDecorated(boolean decorated) {
        isDecorated = decorated;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
