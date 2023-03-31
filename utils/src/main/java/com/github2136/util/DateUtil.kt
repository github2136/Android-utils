package com.github2136.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 *      日期
 *      Created by yubin on 2016/3/11.
 *      日期格式化参数说明 http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
 */
object DateUtil {
    const val SECOND: Long = 1000
    const val MINUTE = SECOND * 60
    const val HOUR = MINUTE * 60
    const val DAY = HOUR * 24
    const val WEEK = DAY * 7

    const val DATE_PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss"
    const val DATE_PATTERN_YMDHM = "yyyy-MM-dd HH:mm"
    const val DATE_PATTERN_YMDH = "yyyy-MM-dd HH"
    const val DATE_PATTERN_YMD = "yyyy-MM-dd"
    const val DATE_PATTERN_YM = "yyyy-MM"
    const val DATE_PATTERN_Y = "yyyy"

    /**
     * 日期转文字
     */
    @JvmStatic
    fun date2str(date: Date?, pattern: String): String {
        return date2str(date, pattern, TimeZone.getDefault().id)
    }

    @JvmStatic
    fun date2str(date: Date?, pattern: String, timeZone: String): String {
        return if (date != null) {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            sdf.format(date)
        } else {
            ""
        }
    }

    /**
     * 文字转日期
     */
    @JvmStatic
    fun str2date(dateStr: String, pattern: String): Date? {
        return str2date(dateStr, pattern, TimeZone.getDefault().id)
    }

    @JvmStatic
    fun str2date(dateStr: String, pattern: String, timeZone: String): Date? {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取现在的时间
     */
    @JvmStatic
    fun getDateNow(): String {
        return getDateNow(DATE_PATTERN_YMDHMS)
    }

    @JvmStatic
    fun getDateNow(pattern: String): String {
        return getDateNow(pattern, TimeZone.getDefault().id)
    }

    @JvmStatic
    fun getDateNow(pattern: String, timeZone: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(Date())
    }

    /**
     * 时区转换
     * @param origin String 源时间
     * @param pattern String 时间格式
     * @param originZone String 源时区
     * @param targetZone String 转换目标时区
     */
    fun timeZoneConversion(origin: String, pattern: String, originZone: String, targetZone: String): String {
        val originDate: Date? = str2date(origin, pattern, originZone)
        return originDate?.let { date2str(it, pattern, targetZone) } ?: ""
    }

    /**
     * 获取相对今天的时间
     */
    @JvmStatic
    fun getRelativeTimeString(date: Date): String {
        val interval: Long //相差时间
        val dateTimeMil = date.time
        val diffTimeMil = System.currentTimeMillis() - dateTimeMil
        val relativeTimeStr: String

        val absDiffTimeMil = Math.abs(diffTimeMil)//绝对时间
        when {
            absDiffTimeMil > DAY * 3 -> relativeTimeStr = date2str(date, DATE_PATTERN_YMD)
            absDiffTimeMil > DAY -> {
                interval = absDiffTimeMil / DAY
                relativeTimeStr = String.format("%d 天%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            absDiffTimeMil > HOUR -> {
                interval = absDiffTimeMil / HOUR
                relativeTimeStr = String.format("%d 小时%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            absDiffTimeMil > MINUTE -> {
                interval = absDiffTimeMil / MINUTE
                relativeTimeStr = String.format("%d 分钟%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            else -> relativeTimeStr = if (diffTimeMil > 0) "刚刚" else "马上"
        }
        return relativeTimeStr
    }

    /**
     * 获取两个时间差距date2必须晚于date1
     */
    @JvmStatic
    fun getRelativeTimeString(date1: Date, date2: Date): String {
        val interval: Long //相差时间
        val dateTimeMil = date1.time
        val diffTimeMil = date2.time - dateTimeMil
        val relativeTimeStr: String

        when {
            diffTimeMil > DAY -> {
                interval = diffTimeMil / DAY
                relativeTimeStr = String.format("%d 天", interval)
            }
            diffTimeMil > HOUR -> {
                interval = diffTimeMil / HOUR
                relativeTimeStr = String.format("%d 小时", interval)
            }
            diffTimeMil > MINUTE -> {
                interval = diffTimeMil / MINUTE
                relativeTimeStr = String.format("%d 分钟", interval)
            }
            else -> {
                interval = diffTimeMil / SECOND
                relativeTimeStr = String.format("%d 秒", interval)
            }
        }
        return relativeTimeStr
    }

    /**
     * 获取两个时间差距date2必须晚于date1
     * @param precision 表示精度
     */
    @JvmStatic
    fun getRelativeTimeString(date1: Date, date2: Date, precision: Long): String {
        var interval: Long //相差时间
        val dateTimeMil = date1.time
        var diffTimeMil = date2.time - dateTimeMil
        val relativeTimeStr = StringBuilder()

        if (precision <= DAY && diffTimeMil > DAY) {
            interval = diffTimeMil / DAY
            relativeTimeStr.append(String.format("%d天", interval))
            diffTimeMil %= DAY
        }
        if (precision <= HOUR && diffTimeMil > HOUR) {
            interval = diffTimeMil / HOUR
            relativeTimeStr.append(String.format("%02d小时", interval))
            diffTimeMil %= HOUR
        }
        if (precision <= MINUTE && diffTimeMil > MINUTE) {
            interval = diffTimeMil / MINUTE
            relativeTimeStr.append(String.format("%02d分钟", interval))
            diffTimeMil %= MINUTE
        }
        if (precision <= SECOND) {
            interval = diffTimeMil / SECOND
            relativeTimeStr.append(String.format("%02d秒", interval))
        }
        return relativeTimeStr.toString()
    }
}

/**
 * 日期转文字
 */
fun Date.str(pattern: String = DateUtil.DATE_PATTERN_YMDHMS, timeZone: String = TimeZone.getDefault().id): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(this)
}

/**
 * 文字转日期
 */
fun String.date(pattern: String = DateUtil.DATE_PATTERN_YMDHMS, timeZone: String = TimeZone.getDefault().id): Date? {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        sdf.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

/**
 * 时间时区转换
 * @receiver String
 * @param pattern String 时间格式
 * @param originZone String 源时区
 * @param targetZone String 转换目标时区
 */
fun String.timeZoneConversion(pattern: String, originZone: String, targetZone: String = TimeZone.getDefault().id): String {
    val originDate: Date? = this.date(pattern, originZone)
    return originDate?.str(pattern, targetZone) ?: ""
}