package com.example.k.superbag2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.receiver.AlarmReceiver;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by K on 2016/8/21.
 */
public class AlarmService extends Service {

    private SharedPreferences sp;


    @Override
    public void onCreate() {
        super.onCreate();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //没设置过，则设置
        if (!sp.getBoolean(Constant.HAS_SET_ALARM,false)){
            List<MemoItem> alarmList = DataSupport.where("isalarm = ?","1").order("alarmTime asc").find(MemoItem.class);
            if (alarmList != null) {
                int index = 0;
                while (index < alarmList.size() && Long.parseLong(alarmList.get(index).getAlarmTime()) >= System.currentTimeMillis()) {
                    index++;
                }
                MemoItem item = alarmList.get(index - 1);
                setAlarm(item);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(Constant.HAS_SET_ALARM, true);
                editor.apply();
            }
        }
        return super.onStartCommand(intent,flags,startId);
    }

    //参数为 需要提醒的具体item
    private void setAlarm(MemoItem item){
        //启动广播接收，发出通知
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(Constant.SET_SOUND,item.isSound());
        intent.putExtra(Constant.SET_SHAKE,item.isShake());
        PendingIntent pt = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(item.getAlarmTime()), pt);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
