package com.github2136.android_utils

import android.app.Application
import com.github2136.util.CrashHandler

/**
 * Created by yb on 2018/10/31.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

         CrashHandler.getInstance(this)?.setCallback(object : CrashHandler.CrashHandlerCallback {
            override fun finishAll() {}

            override fun submitLog(deviceInfo: Map<String, String>, exception: String) {}
        })

    }
}