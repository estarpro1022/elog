package com.example.myapplication.decorator;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class SelectedDayDecorator implements DayViewDecorator {
    private CalendarDay selectedDate = null;
    private boolean decorateSelected = false;
    private RelativeSizeSpan rss =new RelativeSizeSpan(1.4f);
    private RelativeSizeSpan rss2 =new RelativeSizeSpan(1.0f);

    public SelectedDayDecorator() {
        selectedDate = CalendarDay.from(new Date(0));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(selectedDate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if(decorateSelected) {
            view.addSpan(rss);
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new ForegroundColorSpan(Color.BLACK));
        }
        else{
            // 移除装饰效果
            // 不添加装饰效果，或者你可以添加一些取消装饰的逻辑
            view.addSpan(rss2);  // 恢复正常大小
            view.addSpan(new StyleSpan(Typeface.NORMAL));  // 恢复正常样式
            view.addSpan(new ForegroundColorSpan(Color.BLACK));  // 恢复正常颜色，或者设置为你想要的默认颜色
        }

    }

    public void setDecorateSelected(boolean decorateSelected) {
        this.decorateSelected = decorateSelected;
    }
    /**
     *
     */
    public void setDate(Date date) {
        this.selectedDate = CalendarDay.from(date);
    }
}

