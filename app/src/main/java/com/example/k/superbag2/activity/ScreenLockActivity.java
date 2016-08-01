package com.example.k.superbag2.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.LockUtils;
import com.example.k.superbag2.utils.MD5Utils;
import com.example.k.superbag2.view.LockView;

import org.litepal.util.Const;

import java.util.List;

/**
 * Created by Aersasi on 2016/7/30.
 */
public class ScreenLockActivity extends AppCompatActivity {

    private TextView inputTV;

    private Button set_password;
    private Button clean_password;
    private String password;
    private boolean isSet = false;
    private MD5Utils md5Utils;
    private StringBuilder sb;

    private boolean temp = true;
    private String firstPass = "";

    //默认为点进来开始设置密码
    private boolean isFirstSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pic_lock);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstSet = preferences.getBoolean(Constant.FIRST_SET_LOCK,true);
        initView();
    }

    private void initView() {
        /*set_password = (Button) findViewById(R.id.set_password);
        clean_password = (Button) findViewById(R.id.clean_password);*/
        inputTV = (TextView)findViewById(R.id.input_pic_tv);
        final LockView lockView = (LockView) findViewById(R.id.lockView);
        lockView.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {

            @Override
            public boolean onDrawFinished(List<Integer> passPositions) {
                sb = new StringBuilder();
                for (Integer i : passPositions) {
                    sb.append(i.intValue());
                }

                if (!isFirstSet){
                    //直接为解锁
                    inputTV.setText("输入密码");
                    if (LockUtils.getPassword().equals(sb.toString())){
                        MyApplication.setIsLocked(false);
                        finish();
                        return true;
                    } else {
                        Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    //第一次设置密码
                    if (temp){
                        firstPass = sb.toString();
                        lockView.resetPoints();
                        inputTV.setText("再次输入以确认");
                        temp = false;
                        //TODO

                    } else {
                        if (sb.toString().equals(firstPass)){
                            Toast.makeText(ScreenLockActivity.this,"密码设置成功",Toast.LENGTH_SHORT).show();
                            LockUtils.setPassword(firstPass);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ScreenLockActivity.this).edit();
                            editor.putBoolean(Constant.FIRST_SET_LOCK,false);
                            MyApplication.setHasSetLock(true);
                            finish();
                        } else {
                            Toast.makeText(ScreenLockActivity.this,"两次输入密码不同，请重新输入",Toast.LENGTH_SHORT).show();
                            temp = true;
                        }
                    }
                }

                /*if (isSet) {
                    // 比较路径是否一致
                    //sb.toString:输入的数据； md5.GetMD5Code(sb.toString())：加密后的输入数据
                    boolean valid = compare(password, MD5Utils.GetMD5Code(sb.toString()));
                    if (valid) {
                        Toast.makeText(ScreenLockActivity.this, "手势密码正确！", Toast.LENGTH_SHORT).show();
                        MyApplication.isLocked = false;
                        ScreenLockActivity.this.finish();
                    } else {
                        Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                    return valid;
                } else {
                    password = MD5Utils.GetMD5Code(sb.toString());
                    return true;
                }*/


            return true;
            }
        });

    }

    private boolean compare(String password, String input) {
        return password.equals(input);
    }
}