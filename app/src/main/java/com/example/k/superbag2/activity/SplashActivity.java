package com.example.k.superbag2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.R;
import com.example.k.superbag2.utils.LoginUtils;

/**
 * Created by K on 2016/8/13.
 */
public class SplashActivity extends Activity {

    private SwitchHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new SwitchHandler();
        setEvents();
    }

    private void setEvents() {
        if (LoginUtils.getLoginStatus()) {
            //主界面
            handler.sendEmptyMessageDelayed(1, 2500);
        } else {
            //登录界面
            handler.sendEmptyMessageDelayed(2, 2500);
        }
    }

    class SwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    break;
            }
            SplashActivity.this.finish();
        }
    }

}
