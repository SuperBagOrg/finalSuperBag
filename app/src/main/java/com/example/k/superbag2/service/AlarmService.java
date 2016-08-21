package com.example.k.superbag2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.receiver.AlarmReceiver;
import com.example.k.superbag2.utils.AlarmUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by K on 2016/8/21.
 */
public class AlarmService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
//        AlarmUtils alarmUtils = new AlarmUtils();
//        alarmUtils.setAlarm();
        return super.registerReceiver(receiver, filter);
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
