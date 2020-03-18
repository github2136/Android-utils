package com.github2136.util

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager

/**
 * 通用工具类
 */
object CommonUtil {
    @JvmStatic
    fun px2sp(context: Context, sp: Float): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return sp / density + 0.5f
    }

    @JvmStatic
    fun sp2px(context: Context, sp: Float): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return sp * density + 0.5f
    }

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density + 0.5f
    }

    /**
     * app是否安装
     */
    @JvmStatic
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        val installed: Boolean
        installed = try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return installed
    }

    /**
     * 用来判断是否开启通知权限
     */
    @JvmStatic
    fun isNotificationEnabled(context: Context): Boolean {
        val CHECK_OP_NO_THROW = "checkOpNoThrow"
        val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return true
        } else {
            val mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val appInfo = context.applicationInfo
            val pkg = context.applicationContext.packageName
            val uid = appInfo.uid
            var appOpsClass: Class<*>? = null /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass!!.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String::class.java)
                val opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)
                val value = opPostNotificationValue.get(Int::class.java) as Int
                return checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) as Int == AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }

    /**
     * 关闭键盘
     */
    @JvmStatic
    fun closeKeybord(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}