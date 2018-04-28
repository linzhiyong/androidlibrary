package com.lzy.androidlibrary.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * 时间/日期选择对话框
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/22
 * @desc
 */
public class LDateDialogUtil {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm";

    public static enum TYPE {
        /**
         * 日期选择
         */
        DATE,

        /**
         * 时间选择
         */
        TIME,

        /**
         * 日期+时间选择
         */
        DATE_TIME
    }

    private LDateDialogUtil() {
    }

    public interface DateSelectCallback {
        void callback(Calendar calendar);
    }

    /**
     * 日期选择框
     *
     * @param context
     * @param type
     * @param callback
     */
    public static void showDatePickerDialog(final Context context, final TYPE type, final DateSelectCallback callback) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (type == null || type == TYPE.DATE) {
                        if (callback != null) {
                            callback.callback(calendar);
                        }
                    } else {
                        showTimePickerDialog(context, calendar, callback);
                    }
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    /**
     * 时间选择框
     * @param context
     * @param callback
     */
    public static void showTimePickerDialog(Context context, DateSelectCallback callback) {
        Calendar calendar = Calendar.getInstance();
        showTimePickerDialog(context, calendar, callback);
    }

    /**
     * 时间选择框
     * @param context
     * @param calendar
     * @param callback
     */
    public static void showTimePickerDialog(Context context, final Calendar calendar, final DateSelectCallback callback) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
//                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    if (callback != null) {
                        callback.callback(calendar);
                    }
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();
    }

}
