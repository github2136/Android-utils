package com.github2136.android_utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github2136.util.DateUtil;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH,-4);
//         DateUtil.getRelativeTimeString(calendar.getTime());
    }
}
