package com.example.k.superbag2;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.receiver.LockBroadReceiver;

import org.litepal.LitePalApplication;

/**
 * Created by Aersasi on 2016/8/1.
 */
public class MyApplication extends LitePalApplication {
    private IntentFilter intentFilter;
    private LockBroadReceiver lockBroadReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        lockBroadReceiver = new LockBroadReceiver();
        this.registerReceiver(lockBroadReceiver, intentFilter);

    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.unregisterReceiver(lockBroadReceiver);
    }

}
