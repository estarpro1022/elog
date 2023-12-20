package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.interfaces.ApiWeather;
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
        WeatherService.apiGetTemperature();
        assertEquals(0.97, WeatherService.getTemperature());
    }
}