package com.github2136.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.collection.ArrayMap

/**
 * Created by YB on 2019/8/21
 */
@Deprecated("推荐使用Permissionx")
class PermissionUtil(private val activity: Activity) {
    private var mPermissionArrayMap = ArrayMap<String, String>()
    private var mCallback: () -> Unit = {}
    private var setPermission = false//有拒绝且不再提示的权限，打开了应用设置修改权限
    private var has = false//有拒绝的权限
    private val alertDialog by lazy {
        AlertDialog.Builder(activity)
            .setTitle("警告")
            .setCancelable(false)
            .setPositiveButton("请求权限") { _, _ ->
                if (has) {
                    requestPermission()
                } else {
                    setPermission = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                }
            }
            .setNegativeButton("关闭应用") { _, _ -> activity.finish() }
            .create()
    }

    /**
     * 获取权限
     */
    fun getPermission(permissionArrayMap: ArrayMap<String, String>, callback: () -> Unit) {
        this.mPermissionArrayMap = permissionArrayMap
        this.mCallback = callback
        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var permissionStatus = PackageManager.PERMISSION_GRANTED
            //检查权限
            for (per in mPermissionArrayMap) {
                if (activity.checkSelfPermission(per.key) == PackageManager.PERMISSION_DENIED) {
                    //有拒绝权限
                    permissionStatus = PackageManager.PERMISSION_DENIED
                    break
                }
            }
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                requestPermission()
            } else {
                mCallback.invoke()
            }
        } else {
            mCallback.invoke()
        }
    }

    //请求权限
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(
                arrayOf(
                    *mPermissionArrayMap.keys.toTypedArray()
                ), 1
            )
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        var allow = true
        has = false
        val permissionStr = StringBuilder("缺少")
        for ((i, permission) in permissions.withIndex()) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                allow = false
                permissionStr.append(" ${mPermissionArrayMap[permission]}")
                //判断是否点击不再提示
                val showRationale = activity.shouldShowRequestPermissionRationale(permission)
                if (showRationale) {
                    has = true
                }
            }
        }
        permissionStr.append(
            " 权限${
            if (!has) {
                " 请在应用的权限管理中允许以上权限"
            } else {
                ""
            }
            }"
        )
        if (allow) {
            mCallback.invoke()
        } else {
            alertDialog.setMessage(permissionStr)
            alertDialog.show()
        }
    }

    fun onRestart() {
        if (setPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            }
        }
    }
}