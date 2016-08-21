package com.example.k.superbag2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.example.k.superbag2.others.Constant;

/**
 * Created by K on 2016/7/14.
 */
public class AlarmActivity extends BaseActivity {

    private boolean isSound,isShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("提醒了。。。","sdfafd");
        new AlertDialog.Builder(this).setTitle("有提醒")
                .setMessage("今天跟奥巴马约好了喝茶呦!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();

        isSound = getIntent().getBooleanExtra(Constant.SET_SOUND,false);
        isShake = getIntent().getBooleanExtra(Constant.SET_SHAKE,false);
        if (isSound){
            setSound();
        }
        if (isShake){
            setShake();
        }
    }

    private void setSound(){
//        AudioManager am = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    private void setShake(){
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
