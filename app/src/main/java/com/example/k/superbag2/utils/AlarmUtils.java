package com.example.k.superbag2.utils;

import android.util.Log;

import com.example.k.superbag2.bean.MemoItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Aersasi on 2016/8/21.
 */
public class AlarmUtils {
    public static  MemoItem  getNearestMemo(){
        //从数据库获取设置过提醒的memo记录，按提醒时间从小到大排序。
        List<MemoItem> alarmList = DataSupport.where("isalarm = ?","1").order("alarmTime asc").find(MemoItem.class);
        //返回数据库中提醒时间最小的记录、
        return alarmList.get(0);
    }
}
