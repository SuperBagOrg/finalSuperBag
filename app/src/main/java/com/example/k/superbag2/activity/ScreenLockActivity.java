package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.LockUtils;
import com.example.k.superbag2.view.LockView;

import java.util.List;

/**
 * Created by Aersasi on 2016/7/30.
 */
public class ScreenLockActivity extends AppCompatActivity {

    private TextView inputTV;
    private Button cancelBt;

    private Button set_password;
    private Button clean_password;
    private String password;
    private boolean isSet = false;
    private MD5Utils md5Utils;
    private StringBuilder sb;

    private boolean temp = true;
    private String firstPass = "";
    private LockView lockView;


    //默认为点进来开始设置密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pic_lock);
        initView();
    }
    private void initView() {
        lockView = (LockView) findViewById(R.id.lockView);
        inputTV = (TextView)findViewById(R.id.input_pic_tv);
        cancelBt = (Button)findViewById(R.id.pic_lock_cancel_bt);
        final LockView lockView = (LockView) findViewById(R.id.lockView);
        lockView.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {
            @Override
            //true 绘制完成 false 绘制未完成需要重绘
            public boolean onDrawFinished(List<Integer> passPositions) {
                sb = new StringBuilder();
                for (Integer i : passPositions) {
                    sb.append(i.intValue());
                }
                if (MyApplication.isHasSetLock()){
                    //直接为解锁
                    inputTV.setText("输入密码");
                    if (LockUtils.getPassword().equals(sb.toString())){
                        Intent intent = getIntent();
                        if (intent.getBooleanExtra("setting",false)){
                            inputTV.setText("重新设置密码");
                            setPassword();
                        }else {
                            finish();
                        }
                        return true;
                    } else {
                        Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    //第一次设置密码
                    inputTV.setText("输入密码");
                    setPassword();
                }
            return true;
            }
        });
    }
    private void setPassword(){
        if (temp){
            firstPass = sb.toString();
            lockView.resetPoints();
            inputTV.setText("再次输入以确认");
            temp = false;
        } else {
            //第二次
            if (sb.toString().equals(firstPass)){
                Toast.makeText(ScreenLockActivity.this,"密码设置成功",Toast.LENGTH_SHORT).show();
                LockUtils.setPassword(firstPass);
                MyApplication.setHasSetLock(true);
                MyApplication.setLockStyle(Constant.NINEBLOCKLOCK);
                ScreenLockActivity.this.finish();
            } else {
                Toast.makeText(ScreenLockActivity.this,"两次输入密码不同，请重新输入",Toast.LENGTH_SHORT).show();
                temp = true;
            }
        }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean compare(String password, String input) {
        return password.equals(input);
    }
}