package com.github2136.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public static boolean isEquals(String str1, String str2) {
        return (str1 == null && str2 == null) || (str1 != null && str2 != null && str1.equals(str2));
    }

    public static boolean isNotEquals(String str1, String str2) {
        return !isEquals(str1, str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(StringBuilder str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(StringBuilder str) {
        return !isEmpty(str);
    }


    public static boolean isEmpty(StringBuffer str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(StringBuffer str) {
        return !isEmpty(str);
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
}
