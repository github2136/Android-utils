package com.github2136.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * 日志保存
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Application application;
    private static CrashHandler mInstance = new CrashHandler();
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private StringBuffer sb;
    private TreeMap<String, String> map;
    private CrashHandlerCallback mCallback;

    private CrashHandler() {
    }

    /**
     * 单例模式，保证只有一个CustomCrashHandler实例存在
     */
    public static CrashHandler getInstance() {
        return mInstance;
    }

    public void setCustomCrashHanler(Application application, CrashHandlerCallback mCallback) {
        this.application = application;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mCallback = mCallback;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操作
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (BuildConfig.DEBUG && mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            if (mCallback != null) {
                mCallback.finishAll();
            }
        }
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(application, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }
        }.start();
        saveException(application, ex);
        if (mCallback != null) {
            mCallback.submitLog(map, sb.toString());
        }
        return true;
    }

    /**
     * 保存log
     */
    private void saveException(Application application, Throwable ex) {
        StringBuffer sb = getLog(application, ex);
        String filename = FileUtil.createFileName("log", ".log");
        File logFile = new File(FileUtil.getExternalStoragePrivateLogPath(application), filename);
        FileUtil.saveFile(logFile.getPath(), sb.toString());
    }

    private StringBuffer getLog(Application application, Throwable ex) {
        sb = new StringBuffer();
        for (Map.Entry<String, String> entry : getDeviceInfo(application).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));
        return sb;
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     */
    private TreeMap<String, String> getDeviceInfo(Application application) {
        map = new TreeMap<>();
        PackageManager mPackageManager = application.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(application.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (mPackageInfo != null) {
            map.put("versionName", mPackageInfo.versionName);
            map.put("versionCode", "" + mPackageInfo.versionCode);
        }
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("RELEASE", "" + Build.VERSION.RELEASE);
        map.put("CODENAME", "" + Build.VERSION.CODENAME);
        map.put("MODEL", "" + Build.MODEL);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MANUFACTURER", "" + Build.MANUFACTURER);
        map.put("FINGERPRINT", "" + Build.FINGERPRINT);
        return map;
    }

    /**
     * 获取系统未捕捉的错误信息
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        return mStringWriter.toString();
    }

    public interface CrashHandlerCallback {
        void finishAll();

        void submitLog(Map<String, String> deviceInfo, String exception);
    }
}