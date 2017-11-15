package com.xhh.frameworklib.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description:时间日期处理
 * <p>
 * Author: xhh
 * ------>1.新增日期格式处理函数
 * ------>2.抽取 原有日期格式处理函数 并分发
 */
public class DateUtilsBase {
    /**
     * @param date
     * @param datePattern yyyy-MM-dd HH:mm
     */
    public static String formatDate(Date date, String datePattern) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        String ret = dateFormat.format(date);
        return ret;
    }

    /**
     * 格式化时间
     *
     * @param oldFormat
     *            原格式 yyyy-MM-dd HH:mm:ss
     * @param newFormat
     *            转换后的格式
     * @param dateStr
     *            需要转换的字符串
     * @return
     */
    public static String formatDate(String oldFormat, String newFormat,
                                    String dateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

        try {
            return sdf2.format(sdf1.parse(dateStr));
        } catch (Exception e) {
            // e.printStackTrace();
            return dateStr;
        }

    }

    /**
     * 格式化时间
     *
     * @param oldFormat
     *            原格式 yyyy-MM-dd HH:mm:ss
     * @param dateStr
     *            需要转换的字符串
     * @return
     */
    public static String formatTime(String oldFormat,String dateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        try {
            return sdf2.format(sdf1.parse(dateStr));
        } catch (Exception e) {
            // e.printStackTrace();
            return dateStr;
        }

    }

    /**
     * 获取当前Date
     *
     * @return Date类型时间
     */
    public static Date getNowTimeDate() {
        return new Date();
    }

    /**
     * 获取当前时间字符串
     * 格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getNowTimeString() {
        return millis2String(System.currentTimeMillis(), "yyyy年MM月dd日 HH:mm:ss");
    }

    /**
     * @return 日期字符串
     */
    public static String getNowDateString() {
        return millis2String(System.currentTimeMillis(), "yyyy年MM月dd日");
    }

    /**
     * 判断是否同一天
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(String time) {
        return isSameDay(string2Millis(time, "yyyy年MM月dd日"));
    }

    public static boolean isSameDay(Date date) {
        return isSameDay(date.getTime());
    }


    /**
     * 获取当前时间--如：2012-11-06 12:12:10
     */
    public static String getCurrentDate(String formatStr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 返回时间对象;<br/>
     * format为时间格式如("yyyy/MM/dd")<br/>
     * 返回null表示出错了
     */
    public static Date getDate(String time, String format) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 毫秒换成几天前几小时几分钟
     */
    public static String periodToString(Long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        if (day > 0) {
            str = String.valueOf(day) + "天";
        }
        if (hour > 0) {
            str += String.valueOf(hour) + "小时";
        }
        if (minute > 0) {
            str += String.valueOf(minute) + "分钟";
        }
        return str;
    }

    /**
     * 计算几天前;<br/>
     * 传入开始时间(比如"2012/11/06对应format为"yyyy/MM/dd";<br/>
     * 如果返回-1表示格式错误
     */
    public static int getIntervalDays(String beginTime, String format) {
        int dayNum = 0;
        try {
            Date start = getDate(beginTime, format);
            Date now = new Date();
            long res = now.getTime() - start.getTime();
            dayNum = (int) (((res / 1000) / 3600) / 24);
            System.out.println(dayNum + "天前");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return dayNum;
    }

    /**
     * 计算几天前;<br/>
     * 传入开始时间(格式：2012-11-06 12:12:10)<br/>
     * 如果返回-1表示格式错误
     */
    public static int getIntervalDays(String beginTime) {
        return getIntervalDays(beginTime, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 返回当前日期xxxx年x月xx日 星期x
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String mDate = c.get(Calendar.YEAR) + "年" + c.get(Calendar.MONTH) + "月"
            + c.get(Calendar.DATE) + "日  " + weekDays[w];
        return mDate;
    }

    /**
     * 返回当前x月xx日 星期x
     *
     * @return
     */
    public static String getDateNew() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String mDate = c.get(Calendar.MONTH) + 1 + "月"
            + c.get(Calendar.DATE) + "日  ";
        return mDate;
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param day  天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     * 返回天
     *
     * @param date  日期
     * @param date1 日期
     * @return 返回相减后的日期
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 日期相减
     * 返回分钟数
     *
     * @param date  日期
     * @param date1 日期
     * @return 返回相减后的日期
     */
    public static int diffDateMin(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (1000 * 60));
    }

    public static int diffDateMin(Date date, long now) {
        return (int) ((getMillis(date) - now) / (1000 * 60));
    }


    private static String millis2String(long millis, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
    }

    private static long string2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean isSameDay(long millis) {
        long wee = (System.currentTimeMillis() / 86400000) * 86400000 - 8 * 3600000;
        return millis >= wee && millis < wee + 86400000;
    }
}
