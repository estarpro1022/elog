package com.example.myapplication;

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

    public SelectedDayDecorator() {
        selectedDate = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return selectedDate != null && day.equals(selectedDate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }

    /**
     *
     */
    public void setDate(Date date) {
        this.selectedDate = CalendarDay.from(date);
    }
}

