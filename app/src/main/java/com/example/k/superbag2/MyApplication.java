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
    //表示现在程序是否上锁
    private static boolean isLocked = false;
    //表示现在是否设置了锁的功能
    private static boolean hasSetLock = false;
    //表示设置锁的种类 默认：数字锁
    private  static boolean lockStyle = Constant.NUMBERLOCK;

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

    public static boolean isLockStyle() {
        return lockStyle;
    }

    public static void setLockStyle(boolean lockStyle) {
        MyApplication.lockStyle = lockStyle;
    }

    public static boolean isLocked() {
        if (hasSetLock) {
            return isLocked;
        } else {
            return false;
        }
    }

    public static void setIsLocked(boolean isLocked) {
        MyApplication.isLocked = isLocked;
    }

    public static boolean isHasSetLock() {
        return hasSetLock;
    }

    public static void setHasSetLock(boolean hasSetLock) {
        MyApplication.hasSetLock = hasSetLock;
    }

}
