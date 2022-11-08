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
import kotlin.collections.HashMap

/**
 * Created by yb on 2018/10/31.
 */
class Appli : Application() {
    private var mActivitys: MutableList<AppCompatActivity> = mutableListOf()
    override fun onCreate() {
        super.onCreate()

        //自定义Gson
        JsonUtil.dateFormat = "yyyy/MM/dd-HH:mm:ss" //仅修改日期格式

        //完全自定义gson
        JsonUtil.mGson = GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy/MM/dd-HH:mm:ss")
            .create()

        CrashHandler.getInstance(this, BuildConfig.DEBUG)
            .setCallback(object : CrashHandler.CrashHandlerCallback {
                override fun addParam(info: HashMap<String, String>) {
                    info.put("p1", "1111")
                }

                override fun finishAll() {
                    this@Appli.finishAll()
                }

                override fun submitLog(info: Map<String, String>, exception: String) {
                    Log.e("info", info.toString())
                    Log.e("info", exception)
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