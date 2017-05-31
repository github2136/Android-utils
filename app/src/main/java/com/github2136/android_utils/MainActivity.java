package com.github2136.android_utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github2136.util.DownLoadFileUtil;
import com.github2136.util.FileUtil;
import com.github2136.util.SPUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SPUtil spUtilCompat = SPUtilCompat.getInstance(this);
        SPUtil spUtil =SPUtil.getInstance(this,"asdf");
       String s =  FileUtil.externalStoragePrivateRootPath(this,"0000151");
        setContentView(R.layout.activity_main);
//        String ss= FileUtilCompat.externalStorageProjectPath(this);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH,-4);
//         DateUtil.getRelativeTimeString(calendar.getTime());
        DownLoadFileUtil.downloadFile(this, "http://issuecdn.baidupcs.com/issue/netdisk/yunguanjia/BaiduNetdisk_5.5.4.exe", FileUtil.externalStorageRootPath() + File.separator
                + FileUtil.createFileName("jpg"), new DownLoadFileUtil.Callback() {
            @Override
            public void callback(boolean successful, String urlStr, String path) {

            }
        });
    }
}
