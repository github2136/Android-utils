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
//    GetPictureUtil getPictureUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 1000; i++) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(1000);
//                                Log.e("tttt", "t2");
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//        }).start();

        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    ThreadUtil.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.e("threadutil", "index " + finalI);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


        SharedPreferences sp = getSharedPreferences("1", Context.MODE_PRIVATE);
        sp.edit().commit();
        String path1 = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "Android/data/com.kuntu.mobile.fireservices.police/files/Pictures";
        TextUtils.equals("asdf", "asdf");
        TextUtils.equals("asdf", "assdf");
        TextUtils.equals(null, "assdf");
        TextUtils.equals("asdf", null);
        TextUtils.equals(null, null);

        String path2 = FileUtil.getExternalStorageProjectPath(this);
        String path3 = FileUtil.getExternalStoragePrivateRootPath(this, "asdf");
        FileUtil.getExternalStorageProjectPath(this);
        SPUtil.getInstance(this);
//        getPictureUtil = new GetPictureUtil(this, GetPictureUtil.PIC_LIMIT_DISPLAY);
//        getPictureUtil.setCallBack(new GetPictureUtil.GetCallBack() {
//            @Override
//            public void callback(int picCode, String filePath, String extraStr) {
//                Log.e("sss", picCode + "   " + filePath);
//            }
//        });
        Button btn = (Button) findViewById(R.id.btn1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPictureUtil.getPic(1);
//            }
//        });
        List<String> strs = new ArrayList<>();
        strs.add("as1df");
        strs.add("as2df");
        strs.add("as3df");
        strs.add("as4df");
        strs.add("as5df");
        JsonUtil jsonUtil = JsonUtil.getInstance();
        String jsonStr = jsonUtil.toJsonStr(strs);
//        JsonUtil.getInstance().getObjectByStr(jsonStr,new TypeToken<List<String>>());

        Button btnLoadMore = (Button) findViewById(R.id.btn_load_more);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoadMoreActivity.class));
            }
        });
        Button btnListAdapter = (Button) findViewById(R.id.btn_list_adapter);
        btnListAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        Button btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectImageActivity.class);
                intent.putExtra(SelectImageActivity.ARG_SELECT_COUNT, 1);
                startActivityForResult(intent, 0);
            }
        });
    }

    long start, end;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            start = System.currentTimeMillis();
            BitmapUtil.getInstance(data.getStringArrayListExtra(SelectImageActivity.ARG_RESULT).get(0))
                    .limit(1080)
                    .limitSize(1024)
                    .rotation()
                    .save(FileUtil.getExternalStorageRootPath() + "/" + "z" + FileUtil.createFileName(".jpg"), new BitmapUtil.BitmapSaveCallBack() {
                        @Override
                        public void callback(String filePath) {
                            end = System.currentTimeMillis();
                            Log.e("tt", "callback: " + (end - start));
                            Toast.makeText(MainActivity.this, filePath, Toast.LENGTH_SHORT).show();
                            File file = new File(filePath);
                        }
                    });
        }
//        getPictureUtil.onActivityResult(requestCode, resultCode, data);
    }

    private Byte[] toObject(byte[] array) {
        if (array == null)
            return null;
        if (array.length == 0) {
            return new Byte[0];
        }
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Byte.valueOf(array[i]);
        }
        return result;
    }
}
