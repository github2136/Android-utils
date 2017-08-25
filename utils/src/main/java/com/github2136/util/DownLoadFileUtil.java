package com.github2136.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by yubin on 2017/1/18.
 * 下载工具类，可取消删除文件
 */
public class DownLoadFileUtil {
    private static boolean isDownLoad;
    private static boolean isCancel;

    /**
     * 下载文件可取消
     */
    public static void downloadFile(final Context context, final String urlStr, final String path, final Callback callback) {
        final ProgressDialog progressDialog = new ProgressDialog(context) {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isDownLoad) {
                        new AlertDialog.Builder(context).setTitle("警告")
                                .setMessage("是否取消下载，并删除文件？")
                                .setCancelable(false)
                                .setPositiveButton("确定", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isCancel = true;
                                    }
                                }).setNegativeButton("取消", null)
                                .show();
                        return true;
                    }
                }
                return super.onKeyDown(keyCode, event);
            }
        };

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在下载文件");
        progressDialog.show();
        ThreadUtil.getInstance("DownLoadFileUtil").execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                isCancel = false;
                                isDownLoad = true;
                                int index = urlStr.lastIndexOf("/");
                                String encodeFileName = URLEncoder.encode(urlStr.substring(index + 1), "UTF-8");
                                String encodeUrl = urlStr.substring(0, index + 1) + encodeFileName;
                                URL url = new URL(encodeUrl);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(5000);
                                // 获取到文件的大小
                                long max = conn.getContentLength();
                                int sizeType = FileUtil.getFileSizeTypeInt(max);
                                progressDialog.setMax((int) FileUtil.getFileOrFilesSize(max, FileUtil.SIZETYPE_KB));
                                InputStream is = conn.getInputStream();
                                File file = new File(path);
                                file.getParentFile().mkdirs();
                                FileOutputStream fos = new FileOutputStream(file);
                                BufferedInputStream bis = new BufferedInputStream(is);
                                byte[] buffer = new byte[1024];
                                int len;
                                int total = 0;
                                while ((len = bis.read(buffer)) != -1) {
                                    fos.write(buffer, 0, len);
                                    total += len;
                                    // 获取当前下载量
                                    progressDialog.setProgress((int) FileUtil.getFileOrFilesSize(total, FileUtil.SIZETYPE_KB));
                                    progressDialog.setProgressNumberFormat(String.format(Locale.getDefault(), "%1$.02f%3$s/%2$.02f%3$s",
                                            FileUtil.getFileOrFilesSize(total, sizeType),
                                            FileUtil.getFileOrFilesSize(max, sizeType),
                                            FileUtil.getFileSizeTypeStr(sizeType)));
                                    if (isCancel) {
                                        break;
                                    }
                                }
                                fos.close();//关闭流
//                        bis.close();
//                        is.close();
                                if (isCancel) {
                                    file.delete();
                                    call(false, urlStr, path, callback);
                                } else {
                                    call(true, urlStr, path, callback);
                                }
                                isDownLoad = false;
                                progressDialog.dismiss();
                            } else {
                                call(false, urlStr, path, callback);
                                isDownLoad = false;
                            }
                        } catch (IOException e) {
                            progressDialog.dismiss();
                            isDownLoad = false;
                            e.printStackTrace();
                            call(false, urlStr, path, callback);
                        }
                    }
                });
    }

    /**
     * 是否正在下载
     *
     * @return
     */
//    public static boolean isDownloadFile() {return isDownLoad;}

    /**
     * 取消下载
     */
//    public static void cancel() {isCancel = true;}
    private static void call(final boolean successful, final String urlStr, final String path, final Callback callback) {
        if (callback != null) {
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            callback.callback(successful, urlStr, path);
                        }
                    });
        }
    }

    public interface Callback {
        void callback(boolean successful, String urlStr, String path);
    }
}
