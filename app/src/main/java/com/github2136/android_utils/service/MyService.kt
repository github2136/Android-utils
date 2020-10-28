package com.github2136.android_utils.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import com.github2136.util.DateUtil
import com.github2136.util.FileUtil
import java.io.File
import kotlin.concurrent.thread

class MyService : Service() {
    val fileName by lazy {
        File(
            FileUtil.getExternalStorageProjectPath(this),
            "service${DateUtil.getDateNow()}.txt"
        ).absolutePath
    }
    var run = true
    private val mWakeLock by lazy {
        val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
    }

    override fun onCreate() {
        super.onCreate()
        getLock()
    }

    @Synchronized
    fun getLock() {
        mWakeLock.setReferenceCounted(true)
        mWakeLock.acquire()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        run = true
        thread {
            while (run) {
                File(fileName).appendText("${DateUtil.getDateNow()} \n")
                Thread.sleep(5000)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        run = false
        if (mWakeLock.isHeld) {
            mWakeLock.release()
        }
    }
}