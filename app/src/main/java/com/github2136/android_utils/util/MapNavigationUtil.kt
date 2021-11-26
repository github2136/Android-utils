package com.github2136.android_utils.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github2136.util.CommonUtil

/**
 * 自动打开第三方地图导航
 * Created by YB on 2021/11/26
 */
object MapNavigationUtil {
    /**
     * 地图导航
     */
    fun mapNavigation(context: Context, latlng: GPSUtil.UtilLatLng) {
        val amap = CommonUtil.isAppInstalled(context, "com.autonavi.minimap")
        val baidu = CommonUtil.isAppInstalled(context, "com.baidu.BaiduMap")
        val tencent = CommonUtil.isAppInstalled(context, "com.tencent.map")
        val maps = mutableListOf<String>()
        if (amap) {
            maps.add("高德地图")
        }
        if (baidu) {
            maps.add("百度地图")
        }
        if (tencent) {
            maps.add("腾讯地图")
        }
        when {
            maps.size == 1 -> when {
                amap -> openAmap(context, latlng)
                baidu -> openBaiduMap(context, latlng)
                tencent -> openTencent(context, latlng)
            }
            maps.size > 1 -> {
                AlertDialog.Builder(context)
                    .setTitle("选择导航地图")
                    .setItems(maps.toTypedArray()) { _, which ->
                        when (maps[which]) {
                            "高德地图" -> openAmap(context, latlng)
                            "百度地图" -> openBaiduMap(context, latlng)
                            "腾讯地图" -> openTencent(context, latlng)
                        }
                    }
                    .show()
            }
            else -> {
                //下载高德地图
                Toast.makeText(context, "请先下载地图软件", Toast.LENGTH_SHORT).show()
                val uri = Uri.parse("https://wap.amap.com/")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }
    }

    /**
     * 打开百度导航
     * gcj02
     */
    fun openBaiduMap(context: Context, latLng: GPSUtil.UtilLatLng) {
        //http://lbsyun.baidu.com/index.php?title=uri/api/android
        val amap = "baidumap://map/navi?" +
            "location=${latLng.lat},${latLng.lng}&" +
            "coord_type=gcj02&" +
            "src=${context.packageName}"
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(amap)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 打开高德地图导航
     * gcj02
     */
    fun openAmap(context: Context, latLng: GPSUtil.UtilLatLng) {
        //https://lbs.amap.com/api/amap-mobile/guide/android/navigation
        val amap = "androidamap://navi?" +
            "sourceApplication=${context.packageName}&" +
            "lat=${latLng.lat}&" +
            "lon=${latLng.lng}&" +
            "dev=0"
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(amap)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.autonavi.minimap")
        context.startActivity(intent)
    }

    /**
     * 打开腾讯地图
     * gcj02
     */
    fun openTencent(context: Context, latLng: GPSUtil.UtilLatLng) {
        //https://lbs.qq.com/uri_v1/guide-mobile-navAndRoute.html
        val amap = "qqmap://map/routeplan?" +
            "type=drive&" +
            "fromcoord=CurrentLocation&" +
            "tocoord=${latLng.lat},${latLng.lng}&" +
            "referer="
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(amap)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}