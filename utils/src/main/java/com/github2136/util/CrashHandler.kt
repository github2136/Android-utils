package com.github2136.util

import android.Manifest
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.os.Process
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by yb on 2018/9/4.
 */
class CrashHandler private constructor(val application: Application, val debug: Boolean) : Thread.UncaughtExceptionHandler {
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
        e?.let {
            saveException(application, it)
        }
        callback?.submitLog(map, sb.toString())
        if (debug) {
            mDefaultHandler.uncaughtException(t, e)
        } else {
            // 使用Toast来显示异常信息
            thread {
                Looper.prepare()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有权限，存储至外部私有目录
                    Toast.makeText(application, "很抱歉,程序出现异常,即将退出,已将错误日志写至内部目录", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(application, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show()

                }
                Looper.loop()
            }
            try {
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            callback?.finishAll()
            Process.killProcess(Process.myPid())
        }
    }

    /**
     * 保存log
     */
    private fun saveException(application: Application, ex: Throwable) {
        val sb = getLog(ex)
        val filename = FileUtil.createFileName("log", ".txt")
        var logFile = File(FileUtil.getExternalStorageProjectPath(application) + File.separator + "Log", filename)
        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限，存储至外部私有目录
                logFile = File(FileUtil.getExternalStoragePrivateLogPath(application), filename)
            }
        }
        FileUtil.saveFile(logFile.path, sb.toString())
    }

    private fun getLog(ex: Throwable): StringBuffer {
        sb = StringBuffer()
        map.map { entry -> sb.append(entry.key).append(" = ").append(entry.value).append("\n") }
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

        fun getInstance(application: Application, debug: Boolean): CrashHandler {
            if (instance == null) {
                synchronized(CrashHandler::class) {
                    if (instance == null) {
                        instance = CrashHandler(application, debug)
                    }
                }
            }
            return instance!!
        }
    }
}