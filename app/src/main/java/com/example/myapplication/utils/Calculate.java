package com.example.myapplication.utils;

public class Calculate {

    /**
     * calculate Euclidean distance between (a1, b1) and (a2, b2).
     */
    public static float distance(float a1, float b1, float a2, float b2) {
        double square = Math.pow(a1 - a2, 2) + Math.pow(b1 - b2, 2);
        return (float) Math.sqrt(square);
    }

    public static float pixelToDp(float pixel, float density) {
        return pixel / density;
    }
}
