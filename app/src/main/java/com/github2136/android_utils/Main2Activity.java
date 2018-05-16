package com.github2136.android_utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github2136.util.SPUtil;

public class Main2Activity extends AppCompatActivity {
    SPUtil mSpUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mSpUtil = SPUtil.getInstance(this);
        mSpUtil.remove("sss");
        finish();
    }
}
