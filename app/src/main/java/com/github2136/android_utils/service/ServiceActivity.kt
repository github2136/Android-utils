package com.github2136.android_utils.service

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.R
import com.github2136.android_utils.util.SettingUtil
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        btn_battery.setOnClickListener {
            if (SettingUtil.isIgnoringBatteryOptimizations(this)) {
                SettingUtil.requestIgnoreBatteryOptimizations(this)
            } else {
                Toast.makeText(this, "已经加入白名单", Toast.LENGTH_SHORT).show()
            }
        }

        btn_auto_start.setOnClickListener {
            if (SettingUtil.isHuawei) {
                AlertDialog.Builder(this)
                    .setTitle("启动管理设置")
                    .setMessage("找到${resources.getString(R.string.app_name)}，关闭自动管理，设置允许自启动，允许关联启动，允许后台活动")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goHuaweiAutoStartSetting(this)
                    }.show()
            } else if (SettingUtil.isXiaomi) {
                AlertDialog.Builder(this)
                    .setTitle("启动管理设置")
                    .setMessage("找到${resources.getString(R.string.app_name)}，允许自启动")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goXiaomiAutoStartSetting(this)
                    }.show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("启动管理设置")
                    .setMessage("找到启动设置，允许自启动")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goAutoStartSetting(this)
                    }.show()
            }
        }
        btn_power.setOnClickListener {
            if (SettingUtil.isHuawei) {
                AlertDialog.Builder(this)
                    .setTitle("电源设置")
                    .setMessage("关闭省电模式，更多电池设置->休眠时始终保持网络连接，打开")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goHuaweiPowerKeeperSetting(this)
                    }.show()
            } else if (SettingUtil.isXiaomi) {
                AlertDialog.Builder(this)
                    .setTitle("电源设置")
                    .setMessage("找到${resources.getString(R.string.app_name)}，点击打开省电策略，选择无限制")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goXiaomiPowerKeeperSetting(this)
                    }.show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("电源设置")
                    .setMessage("找到电池，设置为无限制或未优化")
                    .setPositiveButton("确定") { _, _ ->
                        SettingUtil.goPowerKeeperSetting(this)
                    }.show()
            }
        }
        btn_start_service.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }
        btn_stop_service.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }
}
