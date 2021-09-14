package com.github2136.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager

/**
 * 通用工具类
 */
object CommonUtil {
    @JvmStatic
    fun px2sp(px: Int): Float {
        val density = Resources.getSystem().displayMetrics.scaledDensity
        return px / density
    }

    @JvmStatic
    fun sp2px(sp: Float): Int {
        val density = Resources.getSystem().displayMetrics.scaledDensity
        return (sp * density).toInt()
    }

    @JvmStatic
    fun px2dp(px: Int): Float {
        val density = Resources.getSystem().displayMetrics.density
        return px / density
    }

    @JvmStatic
    fun dp2px(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
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
}

/**
 * dp2px
 */
val Float.dp2px get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()

/**
 * px2dp
 */
val Int.px2dp get() = this / Resources.getSystem().displayMetrics.density

/**
 * sp2px
 */
val Float.sp2px get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).toInt()

/**
 * px2sp
 */
val Int.px2sp get() = this / Resources.getSystem().displayMetrics.scaledDensity