package com.github2136.android_utils;

import com.github2136.util.DateUtil;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -4);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -10);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -59);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));


          calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 4);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 10);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 59);
        System.out.println(DateUtil.getRelativeTimeString(calendar.getTime()));
    }
}