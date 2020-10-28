package com.github2136.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager


/**
 * 通用工具类
 */
object CommonUtil {
    @JvmStatic
    fun px2sp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return px / density
    }

    @JvmStatic
    fun sp2px(context: Context, sp: Float): Int {
        val density = context.resources.displayMetrics.scaledDensity
        return (sp * density).toInt()
    }

    @JvmStatic
    fun px2dp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
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
     * 关闭键盘
     */
    @JvmStatic
    fun closeKeybord(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        return height
    }
}