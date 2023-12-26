package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.utils.Calculate;

import java.util.regex.Pattern;

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

    @Test
    public void testPhone() {
        Pattern pattern = Pattern.compile("^1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[235-8]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|66\\d{2})\\d{6}$");
        assertEquals(true, pattern.matcher("17826271022").matches());
    }
}