package com.example.k.superbag2.utils;


import android.annotation.TargetApi;
import android.os.Build;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by K on 2016/6/27.
 * 因为Android月份是从0开始的，0代表一月，所以，得到的月份要+1
 */
@TargetApi(Build.VERSION_CODES.N)
public class GetTime {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;

    private Time t;

    public GetTime(){
        t = new Time();
        t.setToNow();
        year = t.year;
        month = t.month;
        min = t.minute;
        hour = t.hour;
        day = t.monthDay;
        sec = t.second;
    }

    public String getMin() {
        if (min < 10){
            return "0"+min;
        }
        return min+"";
    }

    public String getYear() {
        return year+"";
    }

    public String getMonth() {
        if (month+1 < 10){
            return "0"+(month+1);
        } else
            return month+1+"";
    }

    public String getHour() {
        if (hour < 10){
            return "0"+hour;
        }
        return hour+"";
    }

    public String getDay(){
        if (day < 10){
            return "0"+day;
        }
        return day+"";
    }

    public String getSec(){
        if (sec < 10){
            return "0"+sec;
        }
        return sec+"";
    }

    public String getSpecificTime(){
        GetTime gt = new GetTime();
        return gt.getYear()+"-"+gt.getMonth()+"-"+gt.getDay()+" "+gt.getHour()+":"+gt.getMin();
    }

    public String getSpecificTime2Second(){
        GetTime gt = new GetTime();
        return gt.getYear()+gt.getMonth()+gt.getDay()+gt.getHour()+gt.getMin()+gt.getSec();
    }

    /**
     * 获取当前日期是星期几
     *
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(String year,String month,String day) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        //格式必须对应
        String stringDate = year+"-"+month+"-"+day;
        try {
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //判断两个日期相隔的天数
    public String getBetweenDay(String beginDay,String endDay){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long betweenDay = 0;
        try {
            Date begin = format.parse(beginDay);
            Date end = format.parse(endDay);
            betweenDay = (end.getTime() - begin.getTime()) / (1000*60*60*24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return betweenDay+1+"";
    }

    public String getMonthEn(){
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dec";
        }
    }
}
