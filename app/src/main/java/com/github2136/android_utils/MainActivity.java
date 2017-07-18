package com.github2136.android_utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
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

        String path1 = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "Android/data/com.kuntu.mobile.fireservices.police/files/Pictures";

        String path2 =FileUtil.getExternalStorageProjectPath(this);
        String path3 =FileUtil.getExternalStoragePrivateRootPath(this,"asdf");


//        File f = new File(path);
//
//        if (f.exists()) {
//            if (f.isDirectory()) {
//                Uri u = Uri.fromFile(f);
//                Intent intent = new Intent(Intent.ACTION_PICK, u);
//                startActivityForResult(intent, 0);
//            }
//        }

//        Intent it = new Intent(Intent.ACTION_VIEW);
//        Uri mUri = Uri.parse("file://"+file.getPath());
//        it.setDataAndType(mUri, "image/*");
//        startActivity(it);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 0);
//        Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.peo.People.CONTENT_URI);
////        intent.setType("image/*");
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 0);

//        SPUtil spUtilCompat = SPUtilCompat.getInstance(this);
//        SPUtil spUtil =SPUtil.getInstance(this,"asdf");
//       String s =  FileUtil.externalStoragePrivateRootPath(this,"0000151");
//        setContentView(R.layout.activity_main);
////        String ss= FileUtilCompat.externalStorageProjectPath(this);
////        Calendar calendar = Calendar.getInstance();
////        calendar.add(Calendar.DAY_OF_MONTH,-4);
////         DateUtil.getRelativeTimeString(calendar.getTime());
//        DownLoadFileUtil.downloadFile(this, "http://issuecdn.baidupcs.com/issue/netdisk/yunguanjia/BaiduNetdisk_5.5.4.exe", FileUtil.externalStorageRootPath() + File.separator
//                + FileUtil.createFileName("jpg"), new DownLoadFileUtil.Callback() {
//            @Override
//            public void callback(boolean successful, String urlStr, String path) {
//
//            }
//        });
    }
}
