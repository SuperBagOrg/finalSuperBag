package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.others.IsReception;

/**
 * Created by Aersasi on 2016/8/1.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.isLocked()) {
            Intent intent = new Intent(this, ScreenLockActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //后台
        if (IsReception.isApplicationBroughtToBackground(MyApplication.getContext())){
            MyApplication.setIsLocked(false);
        }
    }
}
