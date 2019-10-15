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

    const val Date_pattern_default = "yyyy-MM-dd HH:mm:ss"
    const val Date_Pattern_Short1 = "yyyy-MM-dd"


    /**
     * 日期转文字
     */
    @JvmStatic
    fun date2str(date: Date, pattern: String = Date_pattern_default, timeZone: String = TimeZone.getDefault().id): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(date)
    }

    /**
     * 文字转日期
     */
    @JvmStatic
    fun str2date(dateStr: String, pattern: String = Date_pattern_default, timeZone: String = TimeZone.getDefault().id): Date? {
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
    fun getDateNow(pattern: String = Date_pattern_default, timeZone: String = TimeZone.getDefault().id): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(Date())
    }

    /**
     * UTC转换为指定时区时间格式
     */
    @JvmStatic
    fun UTC2GMT(utc: String, timeZone: String = TimeZone.getDefault().id, pattern: String = Date_pattern_default): String {
        val utcDate: Date? = str2date(utc, pattern, TimeZone.getTimeZone("UTC").id)
        return utcDate?.let {
            date2str(it, pattern, timeZone)
        } ?: ""
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
            absDiffTimeMil > DAY * 3 -> relativeTimeStr = date2str(date, Date_Pattern_Short1)
            absDiffTimeMil > DAY     -> {
                interval = absDiffTimeMil / DAY
                relativeTimeStr = String.format("%d 天%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            absDiffTimeMil > HOUR    -> {
                interval = absDiffTimeMil / HOUR
                relativeTimeStr = String.format("%d 小时%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            absDiffTimeMil > MINUTE  -> {
                interval = absDiffTimeMil / MINUTE
                relativeTimeStr = String.format("%d 分钟%s", interval, if (diffTimeMil > 0) "前" else "后")
            }
            else                     -> relativeTimeStr = if (diffTimeMil > 0) "刚刚" else "马上"
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
            diffTimeMil > DateUtil.DAY    -> {
                interval = diffTimeMil / DateUtil.DAY
                relativeTimeStr = String.format("%d 天", interval)
            }
            diffTimeMil > DateUtil.HOUR   -> {
                interval = diffTimeMil / DateUtil.HOUR
                relativeTimeStr = String.format("%d 小时", interval)
            }
            diffTimeMil > DateUtil.MINUTE -> {
                interval = diffTimeMil / DateUtil.MINUTE
                relativeTimeStr = String.format("%d 分钟", interval)
            }
            else                          -> {
                interval = diffTimeMil / DateUtil.SECOND
                relativeTimeStr = String.format("%d 秒", interval)
            }
        }
        return relativeTimeStr
    }
}