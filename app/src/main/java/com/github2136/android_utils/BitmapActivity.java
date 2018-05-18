package com.github2136.android_utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github2136.util.BitmapUtil;
import com.github2136.util.FileUtil;

public class BitmapActivity extends BaseActivity {
    private static final int REQUEST_PIC = 610;
    Button btnChoose;
    ImageView ivImage;

    @Override
    protected int getViewResId() {
        return R.layout.activity_bitmap;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        btnChoose = (Button) findViewById(R.id.btn_choose);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_PIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PIC:
                    String path = FileUtil.getFileAbsolutePath(mContext, data.getData());
                    showProgressDialog("图片压缩中……");
                    BitmapUtil.getInstance(path)
                            .limit(1080)//限制图片尺寸
                            .limitSize(1024)//限制图片容量
                            .rotation()//按图片旋转角度旋转
//                            .values()//获取图片宽高，第一个为款，第二个为高
//                            .correct()//图片是否是正的
//                            .getByte(new BitmapUtil.BitmapByteGetCallBack() {//获取二进制数据
//                                @Override
//                                public void callback(byte[] bytes) { }
//                            })
//                            .getBase64(new BitmapUtil.BitmapBase64GetCallBack() {//获取图片base64字符串
//                                @Override
//                                public void callback(String base64) { }
//                            })
//                            .save("filepath", new BitmapUtil.BitmapSaveCallBack() {//将图片保存至指定路径
//                                @Override
//                                public void callback(String filePath) { }
//                            })
                            .get(new BitmapUtil.BitmapGetCallBack() {//返回bitmap
                                @Override
                                public void callback(Bitmap bitmap) {
                                    dismissDialogDialog();
                                    ivImage.setImageBitmap(bitmap);
                                }
                            });
                    break;
            }
        }
    }
}
