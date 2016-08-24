package com.example.k.superbag2.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.receiver.AlarmReceiver;
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
    //提醒任务更新。在memo数据更改、新建、删除、手机重启时调用。
    public void setAlarm(){
        if (alarmList.size()!= 0){
            Log.d("clock",":"+"setAlarm");
            //返回数据库中提醒时间最小的记录、
            MemoItem item = alarmList.get(0);
            //启动广播接收，发出通知
            Intent intent = new Intent(MyApplication.getContext(), AlarmReceiver.class);
            intent.putExtra(Constant.SET_SOUND,item.isSound());
            intent.putExtra(Constant.SET_SHAKE,item.isShake());
            PendingIntent pt = PendingIntent.getBroadcast(MyApplication.getContext(), 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager)MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(item.getAlarmTime()), pt);
        }

    }
    //用于receiver收到通知提醒之后的处理：1、设置新的提醒任务。2、更改已提醒memo的isAlarm
    public void setAlarmAfterReceive(){
        //列表至少为1
        if (alarmList.size() >1){
            Log.d("clock",":"+"setAlarm receiver set");

            MemoItem item = alarmList.get(1);
            //启动广播接收，发出通知
            Intent intent = new Intent(MyApplication.getContext(), AlarmReceiver.class);
            intent.putExtra(Constant.SET_SOUND,item.isSound());
            intent.putExtra(Constant.SET_SHAKE,item.isShake());
            PendingIntent pt = PendingIntent.getBroadcast(MyApplication.getContext(), 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager)MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(item.getAlarmTime()), pt);
        }

        //已提醒的isAlarm设置为false。
        SaveUtils.setAlarmTime(alarmList.get(0).getUpdateTime());

        ContentValues values = new ContentValues();
        values.put("isAlarm", "0");
        DataSupport.updateAll(MemoItem.class, values, "alarmTime = ?",alarmList.get(0).getAlarmTime());
    }

    public List<MemoItem> getAlarmList(){
        return alarmList;
    }
}
