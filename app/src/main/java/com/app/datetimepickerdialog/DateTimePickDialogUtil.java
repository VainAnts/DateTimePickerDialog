package com.app.datetimepickerdialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateTimePickDialogUtil implements OnDateChangedListener, OnTimeChangedListener {
private DatePicker datePickerStart;
private TimePicker timePickerStart ;
private AlertDialog alertDialog;
private String startTime;
private Activity activity;
private OnSelectFinishedListener mListener; //接口

public interface OnSelectFinishedListener{
    void selectFinished(String startTime);
}
/**
 * 日期时间弹出选择框构造函数
 *
 * @param activity     ：调用的父activity
 */
public DateTimePickDialogUtil(Activity activity) {
    this.activity = activity;
}

public void init(DatePicker datePicker, TimePicker timePicker) {
    Calendar calendar = Calendar.getInstance();
    datePicker.init(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), this);
    timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
    timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
}

/**
 * 弹出日期时间选择框方法
 *
 * @return
 */
public AlertDialog dateTimePickDialog(final OnSelectFinishedListener mListener) {
    this.mListener = mListener;
    LinearLayout dateTimeLayout = (LinearLayout) activity
            .getLayoutInflater().inflate(R.layout.layout_datetime, null);
    datePickerStart = (DatePicker) dateTimeLayout.findViewById(R.id.datepickerstart);
    timePickerStart = (TimePicker) dateTimeLayout.findViewById(R.id.timepickerstart);
    resizePikcer(datePickerStart);
    resizePikcer(timePickerStart);

    init(datePickerStart, timePickerStart);
    timePickerStart.setIs24HourView(true);
    timePickerStart.setOnTimeChangedListener(this);
    alertDialog = new AlertDialog.Builder(activity)
            .setView(dateTimeLayout)
            .setPositiveButton("设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.selectFinished(startTime);
                        }
                    })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

    onDateChanged(null, 0, 0, 0);
    return alertDialog;
}

public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
    onDateChanged(null, 0, 0, 0);
}

public void onDateChanged(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
    // 获得日历实例
    Calendar calendar = Calendar.getInstance();

    calendar.set(datePickerStart.getYear(), datePickerStart.getMonth(),
            datePickerStart.getDayOfMonth(), timePickerStart.getCurrentHour(),
            timePickerStart.getCurrentMinute());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    startTime = sdf.format(calendar.getTime());
}

/**
 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
 *
 * @return Calendar
 */
public Calendar getCalendarByInintData(String initDateTime) {
    Calendar calendar = Calendar.getInstance();

    // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
    String date = spliteString(initDateTime, "日", "index", "front"); // 日期
    String time = spliteString(initDateTime, "日", "index", "back"); // 时间

    String yearStr = spliteString(date, "年", "index", "front"); // 年份
    String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

    String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
    String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

    String hourStr = spliteString(time, ":", "index", "front"); // 时
    String minuteStr = spliteString(time, ":", "index", "back"); // 分

    int currentYear = Integer.valueOf(yearStr.trim()).intValue();
    int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
    int currentDay = Integer.valueOf(dayStr.trim()).intValue();
    int currentHour = Integer.valueOf(hourStr.trim()).intValue();
    int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

    calendar.set(currentYear, currentMonth, currentDay, currentHour,
            currentMinute);
    return calendar;
}

/**
 * 截取子串
 *
 * @param srcStr      源串
 * @param pattern     匹配模式
 * @param indexOrLast
 * @param frontOrBack
 * @return
 */
public static String spliteString(String srcStr, String pattern,
                                  String indexOrLast, String frontOrBack) {
    String result = "";
    int loc = -1;
    if (indexOrLast.equalsIgnoreCase("index")) {
        loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
    } else {
        loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
    }
    if (frontOrBack.equalsIgnoreCase("front")) {
        if (loc != -1)
            result = srcStr.substring(0, loc); // 截取子串
    } else {
        if (loc != -1)
            result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
    }
    return result;
}

/**
 * 调整FrameLayout大小
 *
 * @param tp
 */
private void resizePikcer(FrameLayout tp) {
    List<NumberPicker> npList = findNumberPicker(tp);
    for (NumberPicker np : npList) {
        resizeNumberPicker(np);
    }
}

/**
 * 得到viewGroup里面的numberpicker组件
 *
 * @param viewGroup
 * @return
 */
private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
    List<NumberPicker> npList = new ArrayList<NumberPicker>();
    View child = null;
    if (null != viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            child = viewGroup.getChildAt(i);
            if (child instanceof NumberPicker) {
                npList.add((NumberPicker) child);
            } else if (child instanceof LinearLayout) {
                List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                if (result.size() > 0) {
                    return result;
                }
            }
        }
    }
    return npList;
}

/*
     * 调整numberpicker大小
     */
private void resizeNumberPicker(NumberPicker np) {
    DeviceConfig.setScreenWidthAndHeight(activity);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            DeviceConfig.getScreenWidth() / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
    params.setMargins(10, 0, 10, 0);
    np.setLayoutParams(params);
}

}