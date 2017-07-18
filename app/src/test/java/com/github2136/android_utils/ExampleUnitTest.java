package com.github2136.android_utils;

import android.graphics.Bitmap;

import com.github2136.util.BitmapUtil;
import com.github2136.util.DateUtil;
import com.github2136.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
//        SPUtilCompat spUtilCompat = SPUtilCompat.getInstance()

String s= DateUtil.date2str(new Date(),"yyyy MM dd HH mm ss s");

        JsonUtil jsonUtilCompat = JsonUtilCompat.getInstance();
        Date date = new Date();
        String d1 = jsonUtilCompat.getGson().toJson(date);
        Date date2=null;
        String d2 = jsonUtilCompat.getGson().toJson(date2);
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

        ExampleUnitTest test = new ExampleUnitTest();
        test.setT1("asdf");
        String j = JsonUtil.getInstance().getGson().toJson(test);
        ExampleUnitTest test2 = JsonUtil.getInstance().getObjectByStr(j, ExampleUnitTest.class);
        ExampleUnitTest test1 = JsonUtil.getInstance().getObjectByStr(j, new TypeToken<ExampleUnitTest>() {}.getType());

        String filePath="";
        BitmapUtil.getInstance(filePath).rotation().limit(1024).get(new BitmapUtil.BitmapGetCallBack() {
            @Override
            public void callback(Bitmap bitmap) {

            }
        });

    }

    private String t1;

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }
}