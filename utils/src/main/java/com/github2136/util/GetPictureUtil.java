package com.github2136.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;


/**
 * 建议使用 compile 'com.github.github2136:SelectImage:-SNAPSHOT'
 * 图片选择拍摄工具<br>
 * 选择或拍摄的图片可以设置压缩尺寸<br>
 * getPictureUtil = new GetPictureUtil(this, GetPictureUtil.PIC_LIMIT_DISPLAY);<br>
 * PIC_LIMIT_NO 不压缩 PIC_LIMIT_DISPLAY按屏幕尺寸压缩<br>
 * 重写Activity或Fragment的onActivityResult方法调用getPictureUtil.onActivityResult(requestCode, resultCode, data);<br>
 * 所有选择或拍摄的照片都会将图片转正，防止出现错误方向的图片<br>
 * 默认拍摄、转正、缩小处理过的照片都保存在项目默认外部私有图片路径/Android/data包名/files/Pictures <br>
 * 如果需要更换可先在application中添加名为util_project_path的&lt;meta-data/&#62;<br>
 * requestCode 789不可使用
 */
@Deprecated
public class GetPictureUtil {
    public static final int PIC_LIMIT_NO = -1;
    public static final int PIC_LIMIT_DISPLAY = 0;
    private static final String KEY_IS_CROP = "IS_CROP";
    private static final String KEY_IS_SHOOT = "IS_SHOOT";
    private static final String KEY_ASPECT_X = "ASPECT_X";
    private static final String KEY_ASPECT_Y = "ASPECT_Y";
    private static final String KEY_OUTPUT_X = "OUTPUT_X";
    private static final String KEY_OUTPUT_Y = "OUTPUT_Y";
    private static final String KEY_MAX_VALUE = "MAX_VALUE";
    private static final String KEY_SHOOT_URI = "SHOOT_URI";
    private static final String KEY_CROP_IMG = "CROP_IMG";
    private static final String KEY_PIC_CODE = "PIC_CODE";
    private static final String KEY_EXTRA = "EXTRA";
    private SPUtil mSPUtil;
    private Activity mAct;
    private Fragment mFra;
    private final int mRequestCode = 789;
    private int mPicCode;//用来区分回调返回的路径来自哪个方法
    private boolean isCrop;// 是否需要裁剪
    private boolean isShoot;// 是否是拍摄的照片
    private Uri mShootUri;//拍摄的图片的保存路径
    private int mAspectX; // 裁剪框比例
    private int mAspectY; // 裁剪框比例
    private int mOutputX;// 输出图片大小
    private int mOutputY;// 输出图片大小
    private Uri mCropImg;//裁剪出来的图片
    private int mMaxValue;//图片宽高最大值 0表示把屏幕宽度当最大值，-1表示不限制
    private String extraStr;//原样返回内容
    private ProgressDialog mProgressDialog;//保存图片时的提示
    private GetCallBack mCallBack;
    private String mPhotoPath;//图片保存路径

    public GetPictureUtil(Activity activity, int max) {
        mAct = activity;
        init(max);
    }

    public GetPictureUtil(Fragment fragment, int max) {
        mFra = fragment;
        mAct = mFra.getActivity();
        init(max);
    }

    private void init(int max) {
        switch (max) {
            case PIC_LIMIT_DISPLAY:
                mMaxValue = mAct.getResources().getDisplayMetrics().widthPixels;
                break;
            default:
                mMaxValue = max;
                break;
        }
        mProgressDialog = new ProgressDialog(mAct);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("数据加载中……");
        mSPUtil = SPUtil.getInstance(mAct, "getPicture");
        mSPUtil.edit()
                .putValue(KEY_MAX_VALUE, mMaxValue)
                .apply();
    }

    public GetCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(GetCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * 拍摄照片
     */
    public void getShoot(int picCode) {
        getShoot(picCode, null);
    }

    /**
     * 拍摄照片
     */
    public void getShoot(int picCode, String extra) {
        isCrop = false;
        isShoot = true;
        mPicCode = picCode;
        extraStr = extra;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mShootUri = Uri.fromFile(
                new File(
                        getPhotoPath(),
                        FileUtil.createFileName(".jpg")));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mShootUri);
        mSPUtil.edit()
                .putValue(KEY_IS_CROP, isCrop)
                .putValue(KEY_IS_SHOOT, isShoot)
                .putValue(KEY_PIC_CODE, mPicCode)
                .putValue(KEY_SHOOT_URI, mShootUri.getPath())
                .putValue(KEY_EXTRA, extraStr)
                .apply();
        startActivityForResult(intent, mRequestCode);
    }

    /**
     * 拍摄并裁剪
     */
    public void getShoot(int picCode, int aspectX, int aspectY, int outputX, int outputY) {
        getShoot(picCode, aspectX, aspectY, outputX, outputY, null);
    }

    /**
     * 拍摄并裁剪
     */
    public void getShoot(int picCode, int aspectX, int aspectY, int outputX, int outputY, String extra) {
        isCrop = true;
        isShoot = true;
        mPicCode = picCode;
        mCropImg = null;
        this.mAspectX = aspectX;
        this.mAspectY = aspectY;
        this.mOutputX = outputX;
        this.mOutputY = outputY;
        extraStr = extra;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mShootUri = Uri.fromFile(
                new File(
                        getPhotoPath(),
                        FileUtil.createFileName(".jpg")));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mShootUri);
        mSPUtil.edit()
                .putValue(KEY_IS_CROP, isCrop)
                .putValue(KEY_IS_SHOOT, isShoot)
                .putValue(KEY_PIC_CODE, mPicCode)
                .putValue(KEY_ASPECT_X, mAspectX)
                .putValue(KEY_ASPECT_Y, mAspectY)
                .putValue(KEY_OUTPUT_X, mOutputX)
                .putValue(KEY_OUTPUT_Y, mOutputY)
                .putValue(KEY_SHOOT_URI, mShootUri.getPath())
                .putValue(KEY_EXTRA, extraStr)
                .apply();
        mSPUtil.remove(KEY_CROP_IMG);
        startActivityForResult(intent, mRequestCode);
    }

    /**
     * 选择图片
     */
    public void getPic(int picCode) {
        getPic(picCode, null);
    }

    /**
     * 选择图片
     */
    public void getPic(int picCode, String extra) {
        isCrop = false;
        isShoot = false;
        mPicCode = picCode;
        extraStr = extra;
        mSPUtil.edit()
                .putValue(KEY_IS_CROP, isCrop)
                .putValue(KEY_IS_SHOOT, isShoot)
                .putValue(KEY_PIC_CODE, mPicCode)
                .putValue(KEY_EXTRA, extraStr)
                .apply();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, mRequestCode);
    }

    /**
     * 选择并裁剪
     */
    public void getPic(int picCode, int aspectX, int aspectY, int outputX, int outputY) {
        getPic(picCode, aspectX, aspectY, outputX, outputY, null);
    }

    /**
     * 选择并裁剪
     */
    public void getPic(int picCode, int aspectX, int aspectY, int outputX, int outputY, String extra) {
        isCrop = true;
        isShoot = false;
        mCropImg = null;
        mPicCode = picCode;
        this.mAspectX = aspectX;
        this.mAspectY = aspectY;
        this.mOutputX = outputX;
        this.mOutputY = outputY;
        extraStr = extra;
        mSPUtil.edit()
                .putValue(KEY_IS_CROP, isCrop)
                .putValue(KEY_IS_SHOOT, isShoot)
                .putValue(KEY_PIC_CODE, mPicCode)
                .putValue(KEY_ASPECT_X, mAspectX)
                .putValue(KEY_ASPECT_Y, mAspectY)
                .putValue(KEY_OUTPUT_X, mOutputX)
                .putValue(KEY_OUTPUT_Y, mOutputY)
                .putValue(KEY_EXTRA, extraStr)
                .apply();
        mSPUtil.remove(KEY_CROP_IMG);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, mRequestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        isCrop = mSPUtil.getBoolean(KEY_IS_CROP);
        isShoot = mSPUtil.getBoolean(KEY_IS_SHOOT);
        mPicCode = mSPUtil.getInt(KEY_PIC_CODE);
        mAspectX = mSPUtil.getInt(KEY_ASPECT_X);
        mAspectY = mSPUtil.getInt(KEY_ASPECT_Y);
        mOutputX = mSPUtil.getInt(KEY_OUTPUT_X);
        mOutputY = mSPUtil.getInt(KEY_OUTPUT_Y);
        mMaxValue = mSPUtil.getInt(KEY_MAX_VALUE);
        String shootUri = mSPUtil.getString(KEY_SHOOT_URI);
        extraStr = mSPUtil.getString(KEY_EXTRA);
        if (CommonUtil.isNotEmpty(shootUri)) {
            mShootUri = Uri.fromFile(new File(shootUri));
        }
        String cropImg = mSPUtil.getString(KEY_CROP_IMG);
        if (CommonUtil.isNotEmpty(cropImg)) {
            mCropImg = Uri.fromFile(new File(cropImg));
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCode) {
                if (isCrop) {
                    if (mCropImg == null) {
                        //裁剪
                        if (isShoot) {
                            //拍摄裁剪
                            crop(mShootUri);
                        } else {
                            crop(data.getData());
                        }
                    } else {
                        //裁剪完成 图片
                        callBack(mPicCode, mCropImg.getPath(), extraStr);
                    }
                    return;
                } else {
                    if (isShoot) {
                        //拍摄
                        if (mMaxValue == PIC_LIMIT_NO) {
                            //图片不做缩小
                            if (BitmapUtil.getInstance(mShootUri.getPath()).correct()) {
                                callBack(mPicCode, mShootUri.getPath(), extraStr);
                            } else {
                                File rotationFile = new File(
                                        getPhotoPath(),
                                        FileUtil.createFileName(".jpg"));
                                mProgressDialog.show();
                                BitmapUtil.getInstance(mShootUri.getPath())
                                        .rotation()
                                        .save(rotationFile.getAbsolutePath(),
                                                new BitmapUtil.BitmapSaveCallBack() {
                                                    @Override
                                                    public void callback(String filePath) {
                                                        mProgressDialog.dismiss();
                                                        callBack(mPicCode, filePath, extraStr);
                                                    }
                                                });
                            }
                        } else {
                            //图片缩小
                            File rotationFile = new File(
                                    getPhotoPath(),
                                    FileUtil.createFileName(".jpg"));
                            mProgressDialog.show();
                            BitmapUtil.getInstance(mShootUri.getPath())
                                    .rotation()
                                    .limit(mMaxValue)
                                    .save(rotationFile.getAbsolutePath(),
                                            new BitmapUtil.BitmapSaveCallBack() {
                                                @Override
                                                public void callback(String filePath) {
                                                    mProgressDialog.dismiss();
                                                    callBack(mPicCode, filePath, extraStr);
                                                }
                                            });
                        }
                    } else {
                        //选择
                        String filePath = FileUtil.getFileAbsolutePath(mAct, data.getData());
                        int[] values = BitmapUtil.getInstance(filePath).values();
                        if (Math.min(values[0], values[1]) > 0) {
                            //没有宽高，表示为非图片文件
                            if (mMaxValue == PIC_LIMIT_NO) {
                                //图片不做缩小
                                if (BitmapUtil.getInstance(filePath).correct()) {
                                    callBack(mPicCode, filePath, extraStr);
                                } else {
                                    File rotationFile = new File(
                                            getPhotoPath(),
                                            FileUtil.createFileName(".jpg"));
                                    mProgressDialog.show();
                                    BitmapUtil.getInstance(filePath)
                                            .rotation()
                                            .save(rotationFile.getAbsolutePath(),
                                                    new BitmapUtil.BitmapSaveCallBack() {
                                                        @Override
                                                        public void callback(String filePath) {
                                                            mProgressDialog.dismiss();
                                                            callBack(mPicCode, filePath, extraStr);
                                                        }
                                                    });
                                }
                            } else {
                                //图片缩小
                                File rotationFile = new File(
                                        getPhotoPath(),
                                        FileUtil.createFileName(".jpg"));
                                mProgressDialog.show();
                                BitmapUtil.getInstance(filePath)
                                        .rotation()
                                        .limit(mMaxValue)
                                        .save(rotationFile.getAbsolutePath(),
                                                new BitmapUtil.BitmapSaveCallBack() {
                                                    @Override
                                                    public void callback(String filePath) {
                                                        mProgressDialog.dismiss();
                                                        callBack(mPicCode, filePath, extraStr);
                                                    }
                                                });
                            }
                        } else {
                            callBack(mPicCode, "", extraStr);
                        }
                    }
                }
            }
        }
    }

    private void crop(Uri photoUri) {
        mCropImg = Uri.fromFile(new File(getPhotoPath(), FileUtil.createFileName("" + ".jpg")));
        mSPUtil.edit()
                .putValue(KEY_CROP_IMG, mCropImg.getPath())
                .apply();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", mAspectX);
        intent.putExtra("aspectY", mAspectY);
        intent.putExtra("outputX", mOutputX);
        intent.putExtra("outputY", mOutputY);
        intent.putExtra("scale", true);// 如果选择的图小于裁剪大小则进行放大
        intent.putExtra("scaleUpIfNeeded", true);// 如果选择的图小于裁剪大小则进行放大
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImg);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, mRequestCode);
    }

    /**
     * 获取图片
     *
     * @return
     */
    private String getPhotoPath() {
        if (CommonUtil.isEmpty(mPhotoPath)) {
            try {
                ApplicationInfo applicationInfo = mAct.getPackageManager().getApplicationInfo(mAct.getPackageName(), PackageManager.GET_META_DATA);
                Bundle metaData = applicationInfo.metaData;
                if (metaData != null) {
                    mPhotoPath = metaData.getString("util_photo_path");
                    if (CommonUtil.isNotEmpty(mPhotoPath)) {
                        mPhotoPath = FileUtil.getExternalStorageRootPath() + File.separator + mPhotoPath;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (CommonUtil.isEmpty(mPhotoPath)) {
            mPhotoPath = FileUtil.getExternalStoragePrivatePicPath(mAct);
        }
        return mPhotoPath;
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (intent.resolveActivity(mAct.getPackageManager()) != null) {
            if (mFra != null) {
                mFra.startActivityForResult(intent, requestCode);
            } else {
                mAct.startActivityForResult(intent, requestCode);
            }
        }
    }

    public interface GetCallBack {
        void callback(int picCode, String filePath, String extraStr);
    }

    private void callBack(int picCode, String file, String extraStr) {
        if (mCallBack != null) {
            mCallBack.callback(picCode, file, extraStr);
        }
    }
}