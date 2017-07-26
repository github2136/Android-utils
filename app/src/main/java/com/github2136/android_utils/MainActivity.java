package com.github2136.android_utils;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github2136.sqlutil.TableUtil;
import com.github2136.util.FileUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path1 = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "Android/data/com.kuntu.mobile.fireservices.police/files/Pictures";

        String path2 = FileUtil.getExternalStorageProjectPath(this);
        String path3 = FileUtil.getExternalStoragePrivateRootPath(this, "asdf");


        MyTableDao myTableDao = new MyTableDao(this);
        MyTable table = new MyTable();
        table.setStr("asdf");
        table.setByt((byte) 12);
        table.setByt1((byte) 881);
        table.setBoolea(true);
        table.setBoolea1(false);
        table.setByt1((byte) 5);
        table.setByt1((byte) 9);
        table.setDoubl(25.3);
        table.setDoubl1(255.3);
        table.setFloa(8.3f);
        table.setFloa1(84.3f);
        table.setIntege1(84812);
        table.setIntt(481);
        table.setLon(84812L);
        table.setLon1(8482312L);
        table.setShor((short) 15);
        table.setShor1((short) 87);
        String b="asdfadf";
        byte[]  bs=b.getBytes();
        table.setBytes(bs);

        String b2="dv8w1f";

        Byte[]  bs2=toObject(  b2.getBytes());
        table.setBytes1(bs2);
//        table.setMyColumn2("asdf");
//        table.setMyColumn3(23);

        myTableDao.insert(table);
        List<MyTable> myTables = myTableDao.query();

//        if (myTables.isEmpty()) {
//            Log.e("ads", "asdf");
//        }
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
//        DownLoadFileUtil.downloadFile(this, "http://issuecdn.baidupcs.com/issue/netdisk/yunguanjia/BaiduNetdisk_5.5.4.exe", FileUtil
// .externalStorageRootPath() + File.separator
//                + FileUtil.createFileName("jpg"), new DownLoadFileUtil.Callback() {
//            @Override
//            public void callback(boolean successful, String urlStr, String path) {
//
//            }
//        });
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
