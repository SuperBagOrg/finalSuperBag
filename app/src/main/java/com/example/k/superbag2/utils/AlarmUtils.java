package com.example.k.superbag2.utils;

import com.example.k.superbag2.bean.MemoItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Aersasi on 2016/8/21.
 */
public class AlarmUtils {

    private List<MemoItem> alarmList;

    public AlarmUtils(){
        //从数据库获取设置过提醒的memo记录，按提醒时间从小到大排序。
        alarmList = DataSupport.where("isalarm = ?","1").order("alarmTime asc").find(MemoItem.class);
    }

    public MemoItem  getNearestMemo(){
        //返回数据库中提醒时间最小的记录、
        return alarmList.get(0);
    }


}
