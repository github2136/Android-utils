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
class SPUtil {
    private var sp: SharedPreferences
    private var edit: Edit? = null

    private constructor(context: Context) {
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

    constructor(context: Context, name: String) {
        if (!TextUtils.isEmpty(name)) {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        } else {
            throw NullPointerException("SharedPreferences name is null")
        }
    }

    inner class Edit {
        private val editor: SharedPreferences.Editor = sp.edit()

        fun putValue(key: String, value: String): Edit {
            editor.putString(key, value)
            return this
        }

        fun putValue(key: String, value: Boolean): Edit {
            editor.putBoolean(key, value)
            return this
        }

        fun putValue(key: String, value: Float): Edit {
            editor.putFloat(key, value)
            return this
        }

        fun putValue(key: String, value: Int): Edit {
            editor.putInt(key, value)
            return this
        }

        fun putValue(key: String, value: Long): Edit {
            editor.putLong(key, value)
            return this
        }

        fun putValue(key: String, value: Set<String>): Edit {
            editor.putStringSet(key, value)
            return this
        }

        fun remove(key: String): Edit {
            editor.remove(key)
            return this
        }

        //保存数据，建议使用apply
        fun apply() {
            editor.apply()
        }
    }

    fun get(): SharedPreferences? {
        return sp
    }

    fun edit(): Edit {
        if (edit == null) {
            edit = Edit()
        }
        return edit as Edit
    }

    //查询某值是否存在
    public fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    //删除某值
    fun remove(vararg key: String) {
        val e = edit()
        key.forEach { e.remove(it) }
        e.apply()
    }

    //清理所有
    fun clear() {
        sp.edit()?.clear()?.apply()
    }

    fun getString(key: String, defVal: String? = null): String? {
        return sp.getString(key, defVal)
    }

    fun getBoolean(key: String, defVal: Boolean = false): Boolean {
        return sp.getBoolean(key, defVal)
    }

    fun getFloat(key: String, defVal: Float = 0F): Float {
        return sp.getFloat(key, defVal)
    }

    fun getInt(key: String, defVal: Int = 0): Int {
        return sp.getInt(key, defVal)
    }

    fun getLong(key: String, defVal: Long = 0L): Long {
        return sp.getLong(key, defVal)
    }

    fun getStringSet(key: String, defVal: Set<String>? = null): Set<String>? {
        return sp.getStringSet(key, defVal)
    }

    companion object {
        @Volatile
        var instance: SPUtil? = null

        fun getInstance(context: Context): SPUtil {
            if (instance == null) {
                synchronized(SPUtil::class) {
                    if (instance == null) {
                        instance = SPUtil(context)
                    }
                }
            }
            return instance!!
        }

        fun getInstance(context: Context, name: String) = SPUtil(context, name)
    }
}