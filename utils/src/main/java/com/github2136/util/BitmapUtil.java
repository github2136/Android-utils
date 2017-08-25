package com.github2136.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import com.google.gson.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理<br>
 * 首先设置图片路径BitmapUtil.getInstance(filePath)<br>
 * 然后就可以在通过其他方法来对图片进行处理<br>
 * rotation//图片旋转为正<br>
 * limit(int max)//显示图片最大宽高 0 表示不限制<br>
 * get***()//获取图片的bitmap、base64、byte[]<br>
 * correct()//图片是否为正的<br>
 * values()//获取图片宽高[0]表示为宽[1]表示为高<br>
 * save(String filepath,callback)//保存至指定目录<br>
 */

public class BitmapUtil {
    private final String TAG = getClass().getSimpleName();
    //图片路径
    String mFilePath;
    //旋转角度
    int mDegree;
    //宽高最大值
    int mMax;
    Handler mHandler;
    private volatile static BitmapUtil mBitmapUtil;

    private BitmapUtil() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static BitmapUtil getInstance(String filePath) {
        if (mBitmapUtil == null) {
            synchronized (BitmapUtil.class) {
                if (mBitmapUtil == null) {
                    mBitmapUtil = new BitmapUtil();
                }
            }
        }
        mBitmapUtil.init(filePath);
        return mBitmapUtil;
    }

    /**
     * 数据初始化
     */
    private void init(String filePath) {
        mFilePath = filePath;
        mDegree = 0;
        mMax = 0;
    }

    /**
     * 获取图片
     */
    private Bitmap getBitmap(String filePath, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 按设置获取图片的bitmap
     */
    private Bitmap getBitmap() {
        Bitmap mBitmap;
        if (mMax != 0) {
            int[] values = getBitmapValue(mFilePath);
            int scaleFactor = (int) Math.max(Math.ceil((double) values[0] / mMax), Math.ceil((double) values[1] / mMax));
            int scaleSize;
            if (scaleFactor > 1) {
                int inSampleSize = 1;
                while (inSampleSize << 1 < scaleFactor) {
                    inSampleSize <<= 1;
                }
                scaleSize = inSampleSize;
            } else {
                scaleSize = scaleFactor;
            }
            //使用inSampleSize压缩图片
            mBitmap = getBitmap(mFilePath, scaleSize);
            //如果图片高宽比限制大则使用Matrix再次缩小
            if (mBitmap.getWidth() > mMax || mBitmap.getHeight() > mMax) {
                float scaleW = (float) mMax / mBitmap.getWidth();
                float scaleH = (float) mMax / mBitmap.getHeight();
                float scale = scaleW > scaleH ? scaleH : scaleW;
                mBitmap = getBitmap(mBitmap, scale);
            }
        } else {
            mBitmap = getBitmap(mFilePath, 1);
        }
        if (mDegree > 0) {
            mBitmap = rotateBitmapByDegree(mBitmap, mDegree);
        }
        return mBitmap;
    }

    /**
     * 获得图片宽高信息 [0]:width [1]:height
     */
    private int[] getBitmapValue(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int[] value = new int[2];
        value[0] = options.outWidth;
        value[1] = options.outHeight;
        return value;
    }

    /**
     * 获得图片的旋转角度
     */
    private int getBitmapRotateDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     */
    private Bitmap rotateBitmapByDegree(Bitmap sourceBitmap, int degree) {
        Bitmap retBitmap;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        retBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
                matrix, true);
        sourceBitmap.recycle();
        sourceBitmap = null;
        System.gc();
        return retBitmap;
    }

    /**
     * 压缩至指定尺寸
     *
     * @param sourceBitmap
     * @param scale
     * @return
     */
    private Bitmap getBitmap(Bitmap sourceBitmap, float scale) {
        Bitmap retBitmap;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        retBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
                matrix, true);
        sourceBitmap.recycle();
        sourceBitmap = null;
        System.gc();
        return retBitmap;
    }

    /**
     * 保存图片
     */
    private boolean saveBitmap(Bitmap sourceBitmap, String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
            fileOut = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 公开方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取图片回调
     */
    public interface BitmapGetCallBack {
        void callback(Bitmap bitmap);
    }

    public interface BitmapBase64GetCallBack {
        void callback(String base64);
    }

    public interface BitmapByteGetCallBack {
        void callback(byte[] bytes);
    }

    /**
     * 旋转为正确的方向
     */
    public BitmapUtil rotation() {
        mDegree = getBitmapRotateDegree(mFilePath);
        return this;
    }

    /**
     * 图片是否为正的
     */
    public boolean correct() {
        return getBitmapRotateDegree(mFilePath) == 0;
    }

    /**
     * 获取图片宽高
     */
    public int[] values() {
        return getBitmapValue(mFilePath);
    }

    /**
     * 宽高的最大值
     */
    public BitmapUtil limit(int max) {
        mMax = max;
        return this;
    }

    /**
     * 获取图片
     */
    public void get(final BitmapGetCallBack callBack) {
        ThreadUtil.getInstance(TAG).execute(
                new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap mBitmap = getBitmap();
                        if (callBack != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.callback(mBitmap);
                                }
                            });
                        }
                    }
                });
    }

    public void getByte(final BitmapByteGetCallBack callBack) {
        ThreadUtil.getInstance(TAG).execute(
                new Runnable() {
                    @Override
                    public void run() {
                        byte[] bytes = null;
                        Bitmap mBitmap = getBitmap();
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            baos.flush();
                            baos.close();
                            bytes = baos.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (callBack != null) {
                            final byte[] finalBytes = bytes;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.callback(finalBytes);
                                }
                            });
                        }
                    }
                });
    }

    public void getBase64(final BitmapBase64GetCallBack callBack) {
        ThreadUtil.getInstance(TAG).execute(
                new Runnable() {
                    @Override
                    public void run() {
                        String base64 = null;
                        Bitmap mBitmap = getBitmap();
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            baos.flush();
                            baos.close();
                            base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (callBack != null) {
                            final String finalBase6 = base64;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.callback(finalBase6);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 保存图片回调
     */
    public interface BitmapSaveCallBack {
        void callback(String filePath);
    }

    /**
     * 保存图片
     */
    public void save(final String filePath, final BitmapSaveCallBack callBack) {
        ThreadUtil.getInstance(TAG).execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Bitmap mBitmap = getBitmap();
                        if (mBitmap == null) {
                            if (callBack != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.callback("");
                                    }
                                });
                            }
                        } else {
                            final boolean isSave = saveBitmap(mBitmap, filePath);
                            mBitmap.recycle();
                            mBitmap = null;
                            System.gc();
                            if (callBack != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.callback(isSave ? filePath : "");
                                    }
                                });
                            }
                        }
                    }
                });
    }
}