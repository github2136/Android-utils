package com.github2136.util

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yb on 2018/9/6.
 */
object DateUtil {
    val SECOND: Long = 1000
    val MINUTE = SECOND * 60
    val HOUR = MINUTE * 60
    val DAY = HOUR * 24
    val WEEK = DAY * 7

    val Date_pattern_default = "yyyy-MM-dd HH:mm:ss"
    val Date_Pattern_Short1 = "yyyy-MM-dd"


    /**
     * 日期转文字
     */
    fun date2str(date: Date, pattern: String = ""): String {
        val sdf: SimpleDateFormat = if (!TextUtils.isEmpty(pattern)) {
            SimpleDateFormat(pattern, Locale.CHINA)
        } else {
            SimpleDateFormat(Date_pattern_default, Locale.CHINA)
        }
        return sdf.format(date)
    }

    /**
     * 文字转日期
     */
    fun str2date(dateStr: String, pattern: String = ""): Date? {
        return try {
            val sdf: SimpleDateFormat = if (!TextUtils.isEmpty(pattern)) {
                SimpleDateFormat(pattern, Locale.CHINA)
            } else {
                SimpleDateFormat(Date_pattern_default, Locale.CHINA)
            }
            sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取现在的时间
     */
    fun getDateNow(pattern: String = ""): String {
        val sdf: SimpleDateFormat = if (!TextUtils.isEmpty(pattern)) {
            SimpleDateFormat(pattern, Locale.CHINA)
        } else {
            SimpleDateFormat(Date_pattern_default, Locale.CHINA)
        }
        return sdf.format(Date())
    }

    /**
     * 获取相对今天的时间
     */
    fun getRelativeTimeString(date: Date): String {
        val interval: Long //相差时间
        val dateTimeMil = date.time
        val diffTimeMil = System.currentTimeMillis() - dateTimeMil
        val relativeTimeStr: String

        val absDiffTimeMil = Math.abs(diffTimeMil)//绝对时间
        when {
            absDiffTimeMil > DAY * 3 -> relativeTimeStr = date2str(date, Date_Pattern_Short1)
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
}