package com.example.k.superbag2.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.R;

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
    }
}
