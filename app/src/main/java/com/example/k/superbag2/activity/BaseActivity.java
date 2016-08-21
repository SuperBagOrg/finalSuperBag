package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.others.IsReception;
import com.example.k.superbag2.utils.SaveUtils;

/**
 * Created by Aersasi on 2016/8/1.
 */
public class BaseActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onResume() {
        super.onResume();
        //5.0以上才可以使用该功能
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.new_primary));
        }
        if (SaveUtils.getHasSetLock()&&SaveUtils.getIsLocked()) {
            if (SaveUtils.getLockStyle()){
                intent = new Intent(this, NumLockActivity.class);
            }else {
                intent = new Intent(this, ScreenLockActivity.class);
            }
            SaveUtils.setIsLocked(false);
            startActivity(intent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //后台
        if (!IsReception.isApplicationBroughtToBackground(MyApplication.getContext())){
            SaveUtils.setIsLocked(false);
        }else {
            SaveUtils.setIsLocked(true);
        }
    }
}
