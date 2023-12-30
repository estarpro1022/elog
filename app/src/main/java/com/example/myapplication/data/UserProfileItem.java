package com.example.myapplication.data;

public class UserProfileItem {
    private String title;
    private int iconResId;

    public UserProfileItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
