package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.interfaces.ApiWeather;
import com.example.myapplication.utils.Calculate;
import com.example.myapplication.utils.WeatherService;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testApiWeather() {
//        WeatherService.apiGetTemperature();
//        System.out.println(WeatherService.getTemperature());
//        Double a = 2.0;
//        System.out.println(String.valueOf(a));
        System.out.println(String.format("%.0f", 3.11));
        System.out.println(String.format("%.0f", 3.66));
        System.out.println(String.format("%.0f", -3.64));
        System.out.println(String.format("%.0f", -3.47));
        System.out.println(String.format("%.0f", -3.55));
    }

    @Test
    public void testDistance() {
        assertEquals(5 , Calculate.distance(3, 4, 7, 7), 0.01);
    }
}