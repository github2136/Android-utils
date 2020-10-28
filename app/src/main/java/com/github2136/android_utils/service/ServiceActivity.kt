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
            AlertDialog.Builder(this)
                .setTitle("启动管理设置")
                .setMessage(SettingUtil.getAutoStartSettingStr(this))
                .setPositiveButton("确定") { _, _ ->
                    SettingUtil.goAutoStartSetting(this)
                }.show()
        }
        btn_power.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("电源设置")
                .setMessage(SettingUtil.getBatterySettingStr(this))
                .setPositiveButton("确定") { _, _ ->
                    SettingUtil.goBatterySetting(this)
                }.show()
        }
        btn_start_service.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }
        btn_stop_service.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }
}
