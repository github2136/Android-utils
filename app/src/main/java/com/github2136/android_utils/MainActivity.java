package com.github2136.android_utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


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