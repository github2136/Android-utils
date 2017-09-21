package com.github2136.android_utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github2136.android_utils.load_more.ListActivity;
import com.github2136.android_utils.load_more.LoadMoreActivity;

import com.github2136.util.CommonUtil;
import com.github2136.util.FileUtil;
import com.github2136.util.GetPictureUtil;
import com.github2136.util.JsonUtil;
import com.github2136.util.SPUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GetPictureUtil getPictureUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path1 = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "Android/data/com.kuntu.mobile.fireservices.police/files/Pictures";

        CommonUtil.isEquals("asdf", "asdf");
        CommonUtil.isEquals("asdf", "assdf");
        CommonUtil.isEquals(null, "assdf");
        CommonUtil.isEquals("asdf", null);
        CommonUtil.isEquals(null, null);

        String path2 = FileUtil.getExternalStorageProjectPath(this);
        String path3 = FileUtil.getExternalStoragePrivateRootPath(this, "asdf");
        FileUtil.getExternalStorageProjectPath(this);
        SPUtil.getInstance(this);
        getPictureUtil = new GetPictureUtil(this, GetPictureUtil.PIC_LIMIT_DISPLAY);
        getPictureUtil.setCallBack(new GetPictureUtil.GetCallBack() {
            @Override
            public void callback(int picCode, String filePath, String extraStr) {
                Log.e("sss", picCode + "   " + filePath);
            }
        });
        Button btn = (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureUtil.getPic(1);
            }
        });
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
        Button btnListAdapter= (Button) findViewById(R.id.btn_list_adapter);
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
//                startActivity(new Intent(MainActivity.this, SelectImageActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getPictureUtil.onActivityResult(requestCode, resultCode, data);
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
