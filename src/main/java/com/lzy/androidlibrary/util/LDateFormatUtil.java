package com.lzy.androidlibrary.util;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author linzhiyong
 * @time 2015年12月14日15:33:50
 * @email wflinzhiyong@163.com
 */

@SuppressLint("SimpleDateFormat")
public class LDateFormatUtil {

    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date类型转成String
     *
     * @param date Date date
     * @return String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * Date类型转成String
     *
     * @param date Date date
     * @return String
     */
    public static String dateToString2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String fileDate = sdf.format(date);
        return fileDate;
    }

    /**
     * Date类型转成String
     *
     * @param date    Date date
     * @param pattern String pattern ->yyyy-MM-dd
     * @return String
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * String类型转成Date
     *
     * @param dateStr String dateStr
     * @return Date
     */
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String类型转成Date
     *
     * @param dateStr String dateStr
     * @return Date
     */
    public static Date stringToDate2(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String类型转成Date
     *
     * @param dateStr String dateStr
     * @param pattern String pattern ->yyyy-MM-dd
     * @return Date
     */
    public static Date stringToDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Calendar 格式化为String
     *
     * @param calendar
     * @param format
     * @return
     */
    public static String calendarToString(Calendar calendar, String format) {
        if (calendar == null) {
            return "";
        }
        if (format == null) {
            format = DEFAULT_DATE_FORMAT;
        }
        return DateFormat.format(format, calendar).toString();
    }

    public static String strToString(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_1);
        try {
            Date date = sdf.parse(dateStr);

            SimpleDateFormat sdf1 = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
            return sdf1.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }

}
