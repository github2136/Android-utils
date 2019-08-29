package com.github2136.util

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils

/**
 *      SharedPreferences
 *      在Manifest中添加名为util_sp_name的<meta-data/>
 */
object SPUtil {
    @Volatile
    private var sp: SharedPreferences? = null

    fun getSharedPreferences(context: Context): SharedPreferences {
        if (sp == null) {
            synchronized(SharedPreferences::class) {
                if (sp == null) {
                    var name: String? = null
                    try {
                        val appInfo: ApplicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                        name = appInfo.metaData?.getString("util_sp_name")
                    } catch (e: PackageManager.NameNotFoundException) {
                    }
                    if (!TextUtils.isEmpty(name)) {
                        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
                    } else {
                        throw NullPointerException("SharedPreferences name is null")
                    }
                }
            }
        }
        return sp!!
    }
}