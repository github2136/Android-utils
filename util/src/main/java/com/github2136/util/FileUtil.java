package com.github2136.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件工具 返回的所有根目录都不带斜杠
 */
public class FileUtil {
    private static final String PATH_LOG = "Log";
    private static final String PATH_DOC = "Documents";
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ///////////////////////////////////////////////////////////////////////////
    // 外部存储状态及目录路径
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 外部存储可写
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 外部存储可读
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 外部存储根目录
     */
    public static String externalStorageRootPath() {
        return Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
    }

    /**
     * 外部存储项目目录
     *
     * @return
     */
    public static String externalStorageProjectPath() {
        return Environment.getExternalStorageDirectory().getAbsoluteFile().toString() + "/" + BuildConfig.APPLICATION_ID;
    }

    /**
     * 外部存储私有根目录
     */
    public static String externalStoragePrivateRootPath(Context context) {
        return context.getExternalFilesDir(null).toString();
    }

    /**
     * 外部存储私有缓存目录
     */
    public static String externalStoragePrivateCachePath(Context context) {
        return context.getExternalCacheDir().toString();
    }

    /**
     * 外部存储私有文档目录
     */
    public static String externalStoragePrivateDocPath(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String path = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + PATH_DOC;
            File doc = new File(path);
            if (!doc.exists()) {
                doc.mkdirs();
            }
            return path;
        } else {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        }
    }

    /**
     * 外部存储私有图片目录
     */
    public static String externalStoragePrivatePicPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 外部存储私有日志目录
     */
    public static String externalStoragePrivateLogPath(Context context) {
        String path = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + PATH_LOG;
        File log = new File(path);
        if (!log.exists()) {
            log.mkdirs();
        }
        return path;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 创建文件名称、创建文件
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建文件名称
     *
     * @param suffix 后缀 .jpg .png
     */
    public static String createFileName(String suffix) { return createFileName(null, suffix); }

    /**
     * 创建文件名称
     *
     * @param prefix 前缀
     * @param suffix 后缀 .jpg .png
     */
    public static String createFileName(String prefix, String suffix) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        return (prefix == null ? "" : prefix + "_") + timeStamp + suffix;
    }
    ///////////////////////////////////////////////////////////////////////////
    // 获得文件绝对路径
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换
     */
    @TargetApi(19)
    public static String getFileAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract
                .isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 文件大小
    ///////////////////////////////////////////////////////////////////////////
    public static final int SIZETYPE_B = 1;/*获取文件大小单位为B的double值*/
    public static final int SIZETYPE_KB = 2;/*获取文件大小单位为KB的double值*/
    public static final int SIZETYPE_MB = 3;/*获取文件大小单位为MB的double值*/
    public static final int SIZETYPE_GB = 4;/*获取文件大小单位为GB的double值*/

    /**
     * 获取文件指定文件的指定单位的大小*
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory())
                blockSize = getFileSizes(file);
            else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹大小
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File files[] = f.listFiles();
        for (int i = 0; i < files.length; i++)
            if (files[i].isDirectory()) size = size + getFileSizes(files[i]);
            else size = size + getFileSize(files[i]);
        return size;
    }

    /**
     * 转换文件大小
     */
    private static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) return wrongSize;
        if (fileS < 1024) fileSizeString = df.format((double) fileS) + "B";
        else if (fileS < 1048576)
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        else if (fileS < 1073741824) fileSizeString = df.format((double) fileS / 1048576) + "MB";
        else fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     */
    private static double FormatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}