package com.github2136.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期
 * Created by yubin on 2016/3/11.
 * 日期格式化参数说明 http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
 */
public class DateUtil {
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;

    public static final String Date_pattern_default = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_Pattern_Short1 = "yyyy-MM-dd";

    /**
     * 日期转文字
     */
    public static String date2str(Date date, String... pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf;
        if (pattern != null && pattern.length > 0) {
            sdf = new SimpleDateFormat(pattern[0], Locale.CHINA);
        } else {
            sdf = new SimpleDateFormat(Date_pattern_default, Locale.CHINA);
        }
        return sdf.format(date);
    }

    /**
     * 文字转日期
     */
    public static Date str2date(String dateStr, String... pattern) {
        try {
            SimpleDateFormat sdf;
            if (pattern != null && pattern.length > 0) {
                sdf = new SimpleDateFormat(pattern[0], Locale.CHINA);
            } else {
                sdf = new SimpleDateFormat(Date_pattern_default, Locale.CHINA);
            }
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取现在的时间
     */
    public static String getDateNow(String... pattern) {
        SimpleDateFormat sdf;
        if (pattern != null && pattern.length > 0) {
            sdf = new SimpleDateFormat(pattern[0], Locale.CHINA);
        } else {
            sdf = new SimpleDateFormat(Date_pattern_default, Locale.CHINA);
        }
        return sdf.format(new Date());
    }

    /**
     * 获取相对今天的时间
     */
    public static String getRelativeTimeString(Date date) {
        long interval; //相差时间
        long dateTimeMil = date.getTime();
        long diffTimeMil = System.currentTimeMillis() - dateTimeMil;
        String relativeTimeStr;

        long absDiffTimeMil = Math.abs(diffTimeMil);//绝对时间
        if (absDiffTimeMil > DAY * 3) {
            relativeTimeStr = date2str(date, Date_Pattern_Short1);
        } else if (absDiffTimeMil > DAY) {
            interval = absDiffTimeMil / DAY;
            relativeTimeStr = String.format("%d 天%s", interval, diffTimeMil > 0 ? "前" : "后");
        } else if (absDiffTimeMil > HOUR) {
            interval = absDiffTimeMil / HOUR;
            relativeTimeStr = String.format("%d 小时%s", interval, diffTimeMil > 0 ? "前" : "后");
        } else if (absDiffTimeMil > MINUTE) {
            interval = absDiffTimeMil / MINUTE;
            relativeTimeStr = String.format("%d 分钟%s", interval, diffTimeMil > 0 ? "前" : "后");
        } else {
            relativeTimeStr = diffTimeMil > 0 ? "刚刚" : "马上";
        }
        return relativeTimeStr;
    }
}