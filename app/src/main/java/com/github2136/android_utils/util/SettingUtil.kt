package com.github2136.android_utils.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.github2136.android_utils.R


/**
 * Created by YB on 2020/4/30
 * 保活设置
 * 通用：应用挂锁
 */
object SettingUtil {

    /**
     * 跳转到指定应用的首页
     */
    private fun showActivity(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private fun showActivity(context: Context, packageName: String, activityDir: String) {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityDir)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 判断是否在电池白名单中
     *
     * @param context
     * @return
     */
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        var isIgnoring = true
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }
        return isIgnoring
    }

    /**
     * 加入电池白名单
     *
     * @param context
     */
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + context.packageName)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 厂商判断
    ///////////////////////////////////////////////////////////////////////////
    val isHuawei: Boolean
        get() = if (Build.BRAND == null) {
            false
        } else {
            Build.BRAND.toLowerCase() == "huawei" || Build.BRAND.toLowerCase() == "honor"
        }

    val isXiaomi: Boolean
        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "xiaomi"

    val isOPPO: Boolean
        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "oppo"

    val isVIVO: Boolean
        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "vivo"

//    val isMeizu: Boolean
//        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "meizu"
//    val isSamsung: Boolean
//        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "samsung"
//
//    val isLeTV: Boolean
//        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "letv"
//
//    val isSmartisan: Boolean
//        get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "smartisan"

    ///////////////////////////////////////////////////////////////////////////
    // 打开设置
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    // 华为 打开多任务界面，下滑应用加锁
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 自启动，后台活动
     * 手机管家->应用启动管理->APP->手动管理全部打开
     * APP->手动管理全部打开
     */
    fun goHuaweiAutoStartSetting(context: Context) {
        try {
            showActivity(
                context, "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
        } catch (e: Exception) {
            showActivity(
                context, "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
            )
        }
    }

    fun getHuaweiAutoStartSettingStr(context: Context) =
        "手机管家 -> 应用启动管理 -> ${context.resources.getString(R.string.app_name)} -> 关闭自动管理 -> 手动管理设置全部打开"

    /**
     * 电池管理
     * 手机管家->电池标志->关闭省电模式->更多电池设置->休眠时始终保持网络连接
     * 更多电池设置->休眠时始终保持网络连接
     */
    fun goHuaweiPowerKeeperSetting(context: Context) {
        showActivity(
            context, "com.huawei.systemmanager",
            "com.huawei.systemmanager.power.ui.HwPowerManagerActivity"
        )
    }

    fun getHuaweiPowerKeeperSettingStr(context: Context) = "手机管家 -> 电池标志 -> 关闭省电模式 -> 更多电池设置 -> 休眠时始终保持网络连接"


    ///////////////////////////////////////////////////////////////////////////
    // 小米 打开多任务界面，长按应用加锁
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 自启动
     * 手机管家->应用管理->权限->自启动管理
     * APP->打开允许自启动开关
     */
    fun goXiaomiAutoStartSetting(context: Context) {
        showActivity(
            context, "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }

    fun getXiaomiAutoStartSettingStr(context: Context) = "手机管家 -> 应用管理 -> 权限 -> 自启动管理 -> ${context.resources.getString(R.string.app_name)} -> 允许启动"

    /**
     * 电池管理
     * 手机管家 ->电池与性能->（关闭省电模式）右上齿轮->（锁屏断开数据（从不），锁屏清理内存从不）应用智能省电->APP->省电策略->无限制
     * APP->省电策略->无限制
     */
    fun goXiaomiPowerKeeperSetting(context: Context) {
        showActivity(
            context, "com.miui.powerkeeper",
            "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"
        )
    }

    fun getXiaomiPowerKeeperSettingStr(context: Context) =
        "手机管家 -> 电池与性能 ->（关闭省电模式）右上齿轮 ->（锁屏断开数据（从不），锁屏清理内存从不）应用智能省电 -> ${context.resources.getString(R.string.app_name)} -> 省电策略 -> 无限制"

    ///////////////////////////////////////////////////////////////////////////
    // OPPO
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 权限隐私 -> 自启动管理 -> 允许应用自启动
     *
     * @param context
     */
    fun goOPPOSetting(context: Context) {
        try {
            showActivity(context, "com.coloros.phonemanager")
        } catch (e1: Exception) {
            try {
                showActivity(context, "com.oppo.safe")
            } catch (e2: Exception) {
                try {
                    showActivity(context, "com.coloros.oppoguardelf")
                } catch (e3: Exception) {
                    showActivity(context, "com.coloros.safecenter")
                }
            }
        }
    }

    fun getOPPOSettingStr(context: Context) =
        "手机管家 -> 权限隐私 -> 应用权限管理 -> ${context.resources.getString(R.string.app_name)} -> 允许应用自启动"

    /**
     * 设置页
     */
    fun goOPPOBatterySetting(context: Context) {
        try {
            showActivity(context, "com.coloros.phonemanager")
        } catch (e1: Exception) {
            try {
                showActivity(context, "com.oppo.safe")
            } catch (e2: Exception) {
                try {
                    showActivity(context, "com.coloros.oppoguardelf")
                } catch (e3: Exception) {
                    showActivity(context, "com.coloros.safecenter")
                }
            }
        }
    }

    fun getOPPOBatterySettingStr(context: Context) =
        "设置 -> 电池 -> 耗电保护 -> ${context.resources.getString(R.string.app_name)} -> 关闭后台冻结，关闭自动优化，关闭深度睡眠"

    ///////////////////////////////////////////////////////////////////////////
    // VIVO
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 权限管理 -> 自启动 -> 允许应用自启动
     *
     * @param context
     */
    fun goVIVOSetting(context: Context) {
        showActivity(context, "com.iqoo.secure")
    }

    fun getVIVOSettingStr(context: Context) =
        "i管家 -> 软件管理 -> 自启动管理 -> ${context.resources.getString(R.string.app_name)} -> 允许应用自启动"

    /**
     * 设置页
     */
    fun goVIVOBatterySetting(context: Context) {
        showActivity(context, "com.iqoo.secure")
    }

    fun getVIVOBatterySettingStr(context: Context) =
        "i管家 -> 省电管理 -> 后台高耗电 -> ${context.resources.getString(R.string.app_name)} -> 允许后台运行"


//    /**
//     * 权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
//     *
//     * @param context
//     */
//    fun goMeizuSetting(context: Context) {
//        showActivity(context, "com.meizu.safe")
//    }
//
//    /**
//     * 自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
//     *
//     * @param context
//     */
//    fun goSamsungSetting(context: Context) {
//        try {
//            showActivity(context, "com.samsung.android.sm_cn")
//        } catch (e: Exception) {
//            showActivity(context, "com.samsung.android.sm")
//        }
//    }

//    /**
//     * 自启动管理 -> 允许应用自启动
//     *
//     * @param context
//     */
//    fun goLetvSetting(context: Context) {
//        showActivity(
//            context, "com.letv.android.letvsafe",
//            "com.letv.android.letvsafe.AutobootManageActivity"
//        )
//    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 其他系统
     * 允许自启动，省电策略->无限制或电池优化->未优化或允许后台活动
     */
    fun goOtherSetting(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }

    fun getOtherSettingStr(context: Context) = "设置 -> 自启动管理或权限管理  -> ${context.resources.getString(R.string.app_name)} -> 允许后台运行，允许自启动"

    fun goOtherBatterySetting(context: Context) {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }

    fun getOtherBatterySettingStr(context: Context) = "设置 -> 电池或电量  -> 关闭省电模式 -> 电池优化 -> ${context.resources.getString(R.string.app_name)} -> 不优化"
    /**
     * 自启动后台运行设置
     */
    fun goAutoStartSetting(context: Context) {
        when {
            isHuawei -> goHuaweiAutoStartSetting(context)
            isXiaomi -> goXiaomiAutoStartSetting(context)
            isOPPO   -> goOPPOSetting(context)
            isVIVO   -> goVIVOSetting(context)
            else     -> goOtherSetting(context)
        }
    }

    /**
     * 获取
     */
    fun getAutoStartSettingStr(context: Context) = when {
        isHuawei -> getHuaweiAutoStartSettingStr(context)
        isXiaomi -> getXiaomiAutoStartSettingStr(context)
        isOPPO   -> getOPPOSettingStr(context)
        isVIVO   -> getVIVOSettingStr(context)
        else     -> getOtherSettingStr(context)
    }

    /**
     * 电池管理
     */
    fun goPowerKeeperSetting(context: Context) {
        when {
            isHuawei -> goHuaweiPowerKeeperSetting(context)
            isXiaomi -> goXiaomiPowerKeeperSetting(context)
            isOPPO   -> goOPPOBatterySetting(context)
            isVIVO   -> goVIVOBatterySetting(context)
            else     -> goOtherBatterySetting(context)
        }
    }

    fun getPowerKeeperSettingStr(context: Context) = when {
        isHuawei -> getHuaweiPowerKeeperSettingStr(context)
        isXiaomi -> getXiaomiPowerKeeperSettingStr(context)
        isOPPO   -> getOPPOBatterySettingStr(context)
        isVIVO   -> getVIVOBatterySettingStr(context)
        else     -> getOtherBatterySettingStr(context)
    }
}