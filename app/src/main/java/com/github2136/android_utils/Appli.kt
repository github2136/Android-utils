package com.github2136.android_utils

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github2136.util.CrashHandler
import com.github2136.util.DateUtil
import com.github2136.util.JsonUtil
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import java.util.*

/**
 * Created by yb on 2018/10/31.
 */
class Appli : Application() {
    private var mActivitys: MutableList<AppCompatActivity> = mutableListOf()
    override fun onCreate() {
        super.onCreate()

        //自定义Gson
        JsonUtil.mGson = GsonBuilder()
            .serializeNulls()
            .setDateFormat(DateUtil.Date_pattern_default)
            .create()

        //默认实例操作
        val str = JsonUtil.instance.getGson().toJson(Date())

        JsonUtil.mGson = GsonBuilder()
            .serializeNulls()
            .setDateFormat(DateUtil.Date_Pattern_Short1)
            .create()
        //新实例操作，每次newInstance都会调用有新的对象
        val str2 = JsonUtil.newInstance().getGson().toJson(Date())

        CrashHandler.getInstance(this, !BuildConfig.DEBUG)
            .setCallback(object : CrashHandler.CrashHandlerCallback {
                override fun finishAll() {
                    this@Appli.finishAll()
                }

                override fun submitLog(deviceInfo: Map<String, String>, exception: String) {
                }
            })

    }

    fun addActivity(act: AppCompatActivity) {
        this.mActivitys.add(act)
    }

    fun removeActivity(act: AppCompatActivity) {
        if (mActivitys.contains(act)) {
            mActivitys.remove(act)
        }
    }

    fun finishAll() {
        var i = 0
        val len = mActivitys.size
        while (i < len) {
            val act = mActivitys[i]
            act.finish()
            i++
        }
    }
}