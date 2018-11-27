package com.github2136.util

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.widget.Toast
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * Created by yb on 2018/9/4.
 */
class CrashHandler private constructor(val application: Application) : Thread.UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    private var sb: StringBuffer = StringBuffer()
    private var map: TreeMap<String, String>
    private var callback: CrashHandlerCallback? = null

    init {
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        //设施信息
        map = TreeMap()
        try {
            val mPackageManager = application.packageManager
            val mPackageInfo: PackageInfo = mPackageManager.getPackageInfo(application.packageName, PackageManager.GET_ACTIVITIES)
            map["versionName"] = mPackageInfo.versionName
            map["versionCode"] = "" + mPackageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        map["SDK_INT"] = "" + Build.VERSION.SDK_INT
        map["RELEASE"] = Build.VERSION.RELEASE
        map["CODENAME"] = Build.VERSION.CODENAME
        map["MODEL"] = Build.MODEL
        map["PRODUCT"] = Build.PRODUCT
        map["MANUFACTURER"] = Build.MANUFACTURER
        map["FINGERPRINT"] = Build.FINGERPRINT
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (!handleException(e)) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e)
        } else {
            if (BuildConfig.DEBUG) {
                mDefaultHandler.uncaughtException(t, e)
            }
            try {
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            callback?.finishAll()
        }
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */

    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        // 使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                Toast.makeText(application, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT)
                        .show()
                Looper.loop()
            }
        }.start()
        saveException(application, ex)
        callback?.submitLog(map, sb.toString())
        return true
    }

    /**
     * 保存log
     */
    private fun saveException(application: Application, ex: Throwable) {
        val sb = getLog(ex)
        val filename = FileUtil.createFileName("log", ".log")
        val logFile = File(FileUtil.getExternalStoragePrivateLogPath(application), filename)
        FileUtil.saveFile(logFile.path, sb.toString())
    }

    private fun getLog(ex: Throwable): StringBuffer {
        sb = StringBuffer()
        for (entry in map) {
            val key = entry.key
            val value = entry.value
            sb.append(key).append(" = ").append(value).append("\n")
        }
        sb.append(obtainExceptionInfo(ex))
        return sb
    }

    /**
     * 获取系统未捕捉的错误信息
     */
    private fun obtainExceptionInfo(throwable: Throwable): String {
        val mStringWriter = StringWriter()
        val mPrintWriter = PrintWriter(mStringWriter)
        throwable.printStackTrace(mPrintWriter)
        mPrintWriter.close()
        return mStringWriter.toString()
    }

    fun setCallback(lis: CrashHandlerCallback) {
        callback = lis
    }

    interface CrashHandlerCallback {
        fun finishAll()

        fun submitLog(deviceInfo: Map<String, String>, exception: String)
    }

    companion object {
        @Volatile
        private var instance: CrashHandler? = null

        fun getInstance(application: Application): CrashHandler {
            if (instance == null) {
                synchronized(CrashHandler::class) {
                    if (instance == null) {
                        instance = CrashHandler(application)
                    }
                }
            }
            return instance!!
        }
    }
}