package com.github2136.android_utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github2136.android_utils.load_more.ListActivity;
import com.github2136.android_utils.load_more.LoadMoreActivity;
import com.github2136.selectimamge.activity.SelectImageActivity;
import com.github2136.util.BitmapUtil;
import com.github2136.util.CommonUtil;
import com.github2136.util.FileUtil;
//import com.github2136.util.GetPictureUtil;
import com.github2136.util.JsonUtil;
import com.github2136.util.SPUtil;
import com.github2136.util.ThreadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    Button btnBitmap, btnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        btnBitmap = (Button) findViewById(R.id.btn_bitmap);
        btnBitmap.setOnClickListener(mOnClickListener);
        btnDate = (Button) findViewById(R.id.btn_date);
        btnDate.setOnClickListener(mOnClickListener);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_bitmap:
                    intent = new Intent(context, BitmapActivity.class);
                    break;
                case R.id.btn_date:
                    intent = new Intent(context, DateActivity.class);
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
    };
}