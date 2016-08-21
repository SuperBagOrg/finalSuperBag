package com.example.k.superbag2.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.service.AlarmService;
import com.example.k.superbag2.utils.AlarmUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by K on 2016/8/21.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pt = PendingIntent.getActivity(context,0,intent1,0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(false)
                .setContentTitle("你有新记事提醒")
                .setContentText("测试")
                .setContentIntent(pt)
                .setSmallIcon(R.drawable.memo_blue)
                .setWhen(System.currentTimeMillis());

        manager.notify(1,builder.build());

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(Constant.HAS_SET_ALARM,false);
        editor.apply();

        context.startService(new Intent(context, AlarmService.class));
    }


}
