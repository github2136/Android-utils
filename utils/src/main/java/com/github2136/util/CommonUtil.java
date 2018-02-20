package com.github2136.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * 通用工具类
 */
public class CommonUtil {
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 用来判断是否开启通知权限
     */
    public static boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return true;
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 该Intent是否可执行
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentExisting(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 唯一标识符
     *
     * @param stable 卸载重置
     * @return
     */
    public static String getInstanceID(Context context, boolean stable) {
        String instanceId;
        if (stable) {
            String path = context.getFilesDir() + File.separator + "InstanceID";
            File file = new File(path);
            if (file.exists()) {
                instanceId = FileUtil.readFile(path);
            } else {
                instanceId = UUID.randomUUID().toString();
                FileUtil.saveFile(path, instanceId);
            }
            return instanceId;
        } else {
            StringBuilder msg = new StringBuilder();
            msg.append(Build.BRAND);
            msg.append(Build.DEVICE);
            msg.append(Build.MANUFACTURER);
            msg.append(Build.MODEL);
            msg.append(Build.SERIAL);
            msg.append(BuildConfig.APPLICATION_ID);
            instanceId = UUID.nameUUIDFromBytes(msg.toString().getBytes()).toString();
            return instanceId;
        }
    }
}