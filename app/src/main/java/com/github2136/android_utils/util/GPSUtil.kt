package com.github2136.android_utils.util

import java.math.BigDecimal
import kotlin.math.*

/**
 * Created by YB on 2019/10/17
 * 坐标转换首位为纬度(lat)，第二位为经度(lng)
 */
object GPSUtil {
    private const val pi = 3.1415926535897932384626
    private const val a = 6378245.0
    private const val ee = 0.00669342162296594323

    /**
     * 地球转火星
     */
    fun wgs84_to_Gcj02(lat: Double, lng: Double): Array<Double> {
        var dLat = transformLat(lng - 105.0, lat - 35.0)
        var dLng = transformLng(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLng = dLng * 180.0 / (a / sqrtMagic * cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLng = lng + dLng
        return arrayOf(mgLat, mgLng)
    }

    /**
     * 火星转地球
     */
    fun gcj02_To_Wgs84(lat: Double, lng: Double): Array<Double> {
        val gps = transform(lat, lng)
        val latitude = lat * 2 - gps[0]
        val longitude = lng * 2 - gps[1]
        return arrayOf(latitude, longitude)
    }

    /**
     * 火星转百度
     */
    fun gcj02_To_Bd09(gg_lat: Double, gg_lon: Double): Array<Double> {
        val z = sqrt(gg_lon * gg_lon + gg_lat * gg_lat) + 0.00002 * sin(gg_lat * pi)
        val theta = atan2(gg_lat, gg_lon) + 0.000003 * cos(gg_lon * pi)
        val bd_lon = z * cos(theta) + 0.0065
        val bd_lat = z * sin(theta) + 0.006
        return arrayOf(bd_lat, bd_lon)
    }


    /**
     * 百度转火星
     */
    fun bd09_To_Gcj02(bd_lat: Double, bd_lon: Double): Array<Double> {
        val x = bd_lon - 0.0065
        val y = bd_lat - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * pi)
        val theta = atan2(y, x) - 0.000003 * cos(x * pi)
        val gg_lon = z * cos(theta)
        val gg_lat = z * sin(theta)
        return arrayOf(gg_lat, gg_lon)
    }

    /**
     * 百度转地球
     */
    fun bd09_To_Wgs84(bd_lat: Double, bd_lon: Double): Array<Double> {
        val gcj02 = bd09_To_Gcj02(bd_lat, bd_lon)
        return gcj02_To_Wgs84(gcj02[0], gcj02[1])
    }

    /**
     * 将小数转换为度分秒，返回数组分别为度，分，秒
     */
    fun convertToDegrees(num: String): Array<String> {
        val numBig = BigDecimal(num)
        val degree = numBig.setScale(0, BigDecimal.ROUND_DOWN).toString()
        val minuteBig = numBig.remainder(BigDecimal.ONE).multiply(BigDecimal(60))
        val minute = minuteBig.setScale(0, BigDecimal.ROUND_DOWN).toString()
        val secondBig = minuteBig.remainder(BigDecimal.ONE).multiply(BigDecimal(60))
        val second = secondBig.toString()
        return arrayOf(degree, minute, second)
    }

    /**
     * 将度分秒转小数进制15位小数精度
     */
    fun convertToDecimal(degree: String, minute: String, second: String): String {
        val degreeBig = BigDecimal(degree)
        val minuteBig = BigDecimal(minute)
        val secondBig = BigDecimal(second)

        val t1 = secondBig.divide(BigDecimal(60), 15, BigDecimal.ROUND_HALF_UP)
        val t2 = t1.add(minuteBig).divide(BigDecimal(60), 15, BigDecimal.ROUND_HALF_UP)
        return degreeBig.add(t2).toString()
    }

    /**
     * 经纬度转瓦片图编号
     */
    fun getTileNumber(lat: Double, lon: Double, level: Int): String {
        var xtile = floor((lon + 180) / 360 * (1 shl level)).toInt()
        var ytile = floor((1 - ln(tan(Math.toRadians(lat)) + 1 / cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 shl level)).toInt()
        if (xtile < 0)
            xtile = 0
        if (xtile >= 1 shl level)
            xtile = (1 shl level) - 1
        if (ytile < 0)
            ytile = 0
        if (ytile >= 1 shl level)
            ytile = (1 shl level) - 1
        return "$xtile/$ytile"
    }

    /**
     * 瓦片图坐标转经纬度
     */
    fun getLatLng(x: Int, y: Int, level: Double): String {
        val n = 2.0.pow(level)
        val lng = x / n * 360.0 - 180.0
        var lat = atan(sinh(Math.PI * (1 - 2 * y / n)))
        lat = lat * 180.0 / Math.PI
        return "$lat/$lng"
    }

    private fun transform(lat: Double, lng: Double): Array<Double> {
        var dLat = transformLat(lng - 105.0, lat - 35.0)
        var dLng = transformLng(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLng = dLng * 180.0 / (a / sqrtMagic * cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLng = lng + dLng
        return arrayOf(mgLat, mgLng)
    }

    private fun transformLat(x: Double, y: Double): Double {
        var ret = (-100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(abs(x)))
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * sin(y * pi) + 40.0 * sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * sin(y / 12.0 * pi) + 320 * sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLng(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(abs(x))
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * sin(x * pi) + 40.0 * sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * sin(x / 12.0 * pi) + 300.0 * sin(x / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }
}