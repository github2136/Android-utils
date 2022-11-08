package com.github2136.util

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.os.Process
import android.widget.Toast
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.concurrent.thread

/**
 * Created by yb on 2018/9/4.
 */
class CrashHandler private constructor(val application: Application, val debug: Boolean) : Thread.UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    private var info: HashMap<String, String> //设备及应用信息
    private var callback: CrashHandlerCallback? = null

    init {
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        //设施信息
        info = HashMap()
        try {
            val mPackageManager = application.packageManager
            val mPackageInfo: PackageInfo = mPackageManager.getPackageInfo(application.packageName, PackageManager.GET_ACTIVITIES)
            //APP显示的版本名
            info[APP_VERSION_NAME] = mPackageInfo.versionName
            //APP的版本编号
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info[APP_VERSION_CODE] = "${mPackageInfo.longVersionCode}"
            } else {
                info[APP_VERSION_CODE] = "${mPackageInfo.versionCode}"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        //Android版本Int
        info[SYS_RELEASE_CODE] = "${Build.VERSION.SDK_INT}"
        //Android显示的版本号
        info[SYS_RELEASE_NAME] = Build.VERSION.RELEASE
        //ABI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            info[ABIS] = Build.SUPPORTED_ABIS?.contentToString() ?: ""
        } else {
            info[ABIS] = mutableListOf<String>().apply {
                if (Build.CPU_ABI.isNotEmpty()) {
                    add(Build.CPU_ABI)
                }
                if (Build.CPU_ABI2.isNotEmpty()) {
                    add(Build.CPU_ABI2)
                }
            }.joinToString(prefix = "[", postfix = "]") {
                it
            }
        }
        //主板名称
        info[BOARD] = Build.BOARD
        //手机品牌
        info[BRAND] = Build.BRAND
        //工业品设计外观名称，名称没什么规律
        info[DEVICE] = Build.DEVICE
        //显示给用户的名称
        info[DISPLAY] = Build.DISPLAY
        //设备指纹，由厂商、型号、等内容组成的字符，一般同一型号同一系统手机这个值相同
        info[FINGERPRINT] = Build.FINGERPRINT
        //硬件名称，一般为CPU名称
        info[HARDWARE] = Build.HARDWARE
        //变更列表编号，可能是品牌和设备名
        info[ID] = Build.ID
        //硬件制造商
        info[MANUFACTURER] = Build.MANUFACTURER
        //产品具体型号
        info[MODEL] = Build.MODEL
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        val exception = e?.let { obtainExceptionInfo(it) } ?: ""
        callback?.addParam(info)
        saveException(exception)
        callback?.submitLog(info, exception)
        if (debug) {
            mDefaultHandler.uncaughtException(t, e)
        } else {
            // 使用Toast来显示异常信息
            thread {
                Looper.prepare()
                Toast.makeText(application, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show()
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
    private fun saveException(ex: String) {
        val sb = StringBuffer()
        info.map { entry -> sb.append(entry.key).append(" = ").append(entry.value).append("\n") }
        val filename = FileUtil.createFileName("log", ".txt")
        //存储至外部私有目录
        File(FileUtil.getExternalStoragePrivateLogPath(application), filename).writeText(sb.toString() + ex)
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
        /**
         * 添加KV参数
         */
        fun addParam(info: HashMap<String, String>)
        /**
         * 结束所有界面
         */
        fun finishAll()

        /**
         * 提交保存异常
         */
        fun submitLog(info: Map<String, String>, exception: String)
    }

    companion object {
        @Volatile
        private var instance: CrashHandler? = null

        //APP的版本编号
        const val APP_VERSION_CODE = "APP_VERSION_CODE"
        //APP显示的版本名
        const val APP_VERSION_NAME = "APP_VERSION_NAME"
        //Android版本Int
        const val SYS_RELEASE_CODE = "SYS_RELEASE_CODE"
        //Android显示的版本号
        const val SYS_RELEASE_NAME = "SYS_RELEASE_NAME"
        //ABI
        const val ABIS = "ABIS"
        //主板名称
        const val BOARD = "BOARD"
        //手机品牌
        const val BRAND = "BRAND"
        //工业品设计外观名称，名称没什么规律
        const val DEVICE = "DEVICE"
        //显示给用户的名称
        const val DISPLAY = "DISPLAY"
        //设备指纹，由厂商、型号、等内容组成的字符，一般同一型号同一系统手机这个值相同
        const val FINGERPRINT = "FINGERPRINT"
        //硬件名称，一般为CPU名称
        const val HARDWARE = "HARDWARE"
        //变更列表编号，可能是品牌和设备名
        const val ID = "ID"
        //硬件制造商
        const val MANUFACTURER = "MANUFACTURER"
        //产品具体型号
        const val MODEL = "MODEL"

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