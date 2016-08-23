package com.example.k.superbag2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.k.superbag2.utils.AlarmUtils;


/**
 * Created by Aersasi on 2016/8/22.
 * 开机启动服务AlarmService（）更新提醒时间
 */
public class BootStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmUtils alarmUtils = new AlarmUtils();
            alarmUtils.setAlarm();
        }
    }
}
