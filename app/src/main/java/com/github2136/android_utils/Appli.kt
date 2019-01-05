package com.github2136.android_utils

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github2136.util.CrashHandler

/**
 * Created by yb on 2018/10/31.
 */
class Appli : Application() {
    private var mActivitys: MutableList<AppCompatActivity> = mutableListOf()
    override fun onCreate() {
        super.onCreate()

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
        this.mActivitys!!.add(act)
    }

    fun removeActivity(act: AppCompatActivity) {
        if (mActivitys!!.contains(act)) {
            mActivitys!!.remove(act)
        }
    }

    fun finishAll() {
        var i = 0
        val len = mActivitys!!.size
        while (i < len) {
            val act = mActivitys!![i]
            act.finish()
            i++
        }
    }
}