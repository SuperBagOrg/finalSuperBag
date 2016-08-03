package com.example.k.superbag2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.activity.NumLockActivity;
import com.example.k.superbag2.activity.ScreenLockActivity;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.others.IsReception;

/**
 * Created by Aersasi on 2016/7/31.
 */
public class LockBroadReceiver extends BroadcastReceiver {

    private Intent intent_startLock;

    @Override
    public void onReceive(Context context, Intent intent) {

            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) && MyApplication.isHasSetLock()) {
                if (!MyApplication.isLockStyle()){
                    intent_startLock = new Intent(context,ScreenLockActivity.class);
                }else {
                    intent_startLock = new Intent(context,NumLockActivity.class);
                }

                intent_startLock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent_startLock);
            }else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
//                Intent intent_startLock = new Intent(context,ScreenLockActivity.class);
//                intent_startLock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent_startLock);
            }

    }

}

