package com.github2136.android_utils.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.text.DecimalFormat
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
    fun wgs84_to_gcj02(origin: UtilLatLng): UtilLatLng {
        var dLat = transformLat(origin.lng - 105.0, origin.lat - 35.0)
        var dLng = transformLng(origin.lng - 105.0, origin.lat - 35.0)
        val radLat = origin.lat / 180.0 * pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLng = dLng * 180.0 / (a / sqrtMagic * cos(radLat) * pi)
        val mgLat = origin.lat + dLat
        val mgLng = origin.lng + dLng
        return UtilLatLng(mgLat, mgLng)
    }

    /**
     * 地球转百度
     */
    fun wgs84_to_bd09(origin: UtilLatLng): UtilLatLng {
        val gcj02 = wgs84_to_gcj02(origin)
        return gcj02_to_bd09(gcj02)
    }

    /**
     * 火星转地球
     */
    fun gcj02_to_wgs84(origin: UtilLatLng): UtilLatLng {
        val gps = transform(origin)
        val latitude = origin.lat * 2 - gps.lat
        val longitude = origin.lng * 2 - gps.lng
        return UtilLatLng(latitude, longitude)
    }

    /**
     * 火星转百度
     */
    fun gcj02_to_bd09(origin: UtilLatLng): UtilLatLng {
        val z = sqrt(origin.lng * origin.lng + origin.lat * origin.lat) + 0.00002 * sin(origin.lat * pi)
        val theta = atan2(origin.lat, origin.lng) + 0.000003 * cos(origin.lng * pi)
        val bd_lon = z * cos(theta) + 0.0065
        val bd_lat = z * sin(theta) + 0.006
        return UtilLatLng(bd_lat, bd_lon)
    }


    /**
     * 百度转火星
     */
    fun bd09_to_gcj02(origin: UtilLatLng): UtilLatLng {
        val x = origin.lng - 0.0065
        val y = origin.lat - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * pi)
        val theta = atan2(y, x) - 0.000003 * cos(x * pi)
        val gg_lon = z * cos(theta)
        val gg_lat = z * sin(theta)
        return UtilLatLng(gg_lat, gg_lon)
    }

    /**
     * 百度转地球
     */
    fun bd09_to_wgs84(origin: UtilLatLng): UtilLatLng {
        val gcj02 = bd09_to_gcj02(origin)
        return gcj02_to_wgs84(gcj02)
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
        val second = String.format("%.2f", secondBig.toDouble())
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
     * 经纬度转瓦片图编号（墨卡托）
     */
    fun getTileNumberW(lat: Double, lng: Double, zoom: Int): UtilTile {
        var xtile = floor((lng + 180) / 360 * (1 shl zoom)).toInt()
        var ytile = floor((1 - ln(tan(Math.toRadians(lat)) + 1 / cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 shl zoom)).toInt()
        if (xtile < 0)
            xtile = 0
        if (xtile >= 1 shl zoom)
            xtile = (1 shl zoom) - 1
        if (ytile < 0)
            ytile = 0
        if (ytile >= 1 shl zoom)
            ytile = (1 shl zoom) - 1
        return UtilTile(xtile, ytile, zoom)
    }

    /**
     * 瓦片图坐标转经纬度（墨卡托）
     */
    fun getLatLngW(x: Int, y: Int, zoom: Int): UtilLatLng {
        val n = 2.0.pow(zoom)
        val lng = x / n * 360.0 - 180.0
        var lat = atan(sinh(Math.PI * (1 - 2 * y / n)))
        lat = lat * 180.0 / Math.PI
        return UtilLatLng(lat, lng)
    }

    /**
     * 经纬度转瓦片图编号（经纬度投影）
     */
    fun getTileNumberC(lat: Double, lng: Double, zoom: Int): UtilTile {
        val n = 2.0.pow(zoom)
        val pixel = 360 / (n * 256)
        val xtile = floor((lng + 180) / pixel / 256).toInt()
        val ytile = floor((90 - lat) / pixel / 256).toInt()
        return UtilTile(xtile, ytile, zoom)
    }


    private fun transform(origin: UtilLatLng): UtilLatLng {
        var dLat = transformLat(origin.lng - 105.0, origin.lat - 35.0)
        var dLng = transformLng(origin.lng - 105.0, origin.lat - 35.0)
        val radLat = origin.lat / 180.0 * pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLng = dLng * 180.0 / (a / sqrtMagic * cos(radLat) * pi)
        val mgLat = origin.lat + dLat
        val mgLng = origin.lng + dLng
        return UtilLatLng(mgLat, mgLng)
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

    private val latlngDecimalFormat by lazy { DecimalFormat("#0.000000") }

    /**
     * 返回不同格式经纬度字符串
     */
    fun getLatlngStr(latLng: UtilLatLng, type: Int = 0): String {
        return when (type) {
            0 -> {
                "${latlngDecimalFormat.format(latLng.lng.absoluteValue)}${if (latLng.lng >= 0) "E" else "W"},${latlngDecimalFormat.format(latLng.lat.absoluteValue)}${if (latLng.lat >= 0) "N" else "S"}"
            }
            else -> {
                val longitude = convertToDegrees(latLng.lng.absoluteValue.toString())
                val latitude = convertToDegrees(latLng.lat.absoluteValue.toString())
                "${longitude[0]}°${longitude[1]}'${longitude[2]}''${if (latLng.lng >= 0) "E" else "W"},${latitude[0]}°${latitude[1]}'${latitude[2]}''${if (latLng.lat >= 0) "N" else "S"}"
            }
        }
    }

    @Parcelize
    data class UtilLatLng(
        /**纬度*/
        val lat: Double,
        /**经度*/
        val lng: Double) : Parcelable

    @Parcelize
    data class UtilTile(val x: Int, val y: Int, val z: Int) : Parcelable
}