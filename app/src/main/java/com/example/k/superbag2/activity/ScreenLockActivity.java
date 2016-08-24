package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.MD5Utils;
import com.example.k.superbag2.utils.SaveUtils;
import com.example.k.superbag2.view.LockView;

import org.litepal.util.Const;

import java.util.List;

/**
 * Created by Aersasi on 2016/7/30.
 */
public class ScreenLockActivity extends AppCompatActivity {

    private TextView inputTV;
    private Button cancelBt;
    private StringBuilder sb;
    private boolean temp = true;
    private boolean temp_change = true;
    private String firstPass = "";
    private LockView lockView;
    private Intent intent;

    //默认为点进来开始设置密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pic_lock);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = getIntent();
        if (SaveUtils.getHasSetLock()&&intent.getBooleanExtra("setting",false)&&SaveUtils.getLockStyle()
                &&temp_change){
            Intent getNumLock = new Intent(ScreenLockActivity.this,NumLockActivity.class);
            startActivity(getNumLock);
            temp_change = false;
            Log.d("change","block---------->num");
        }
    }

    //加锁界面。重写BACk的点击响应，改为回到桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        Intent intent = getIntent();

        switch (key) {

            case KeyEvent.KEYCODE_BACK: {
                if (intent.getBooleanExtra("setting",false)){
                    finish();
                    return true;
                }else {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        lockView = (LockView) findViewById(R.id.lockView);

        lockView.getLayoutParams();
        inputTV = (TextView) findViewById(R.id.input_pic_tv);
        cancelBt = (Button) findViewById(R.id.pic_lock_cancel_bt);
        //添加取消按钮，点击回到桌面。
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });
        final LockView lockView = (LockView) findViewById(R.id.lockView);
        //密码绘制
        lockView.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {
            @Override
            //true 绘制完成，绘制路径依旧为蓝色 false 绘制未完成需要重绘，绘制路径变为红色
            public boolean onDrawFinished(List<Integer> passPositions) {
                sb = new StringBuilder();
                for (Integer i : passPositions) {
                    sb.append(i.intValue());
                }
                //最常见的情形：从外部进入解锁界面
                if (!intent.getBooleanExtra("setting",false)&&SaveUtils.getHasSetLock()) {
                    inputTV.setText("输入密码");
                    if (SaveUtils.getPassword().equals(sb.toString())) {
                        if (intent.getBooleanExtra("lock",false)){
                            SaveUtils.setHasSetLock(false);
                        }
                        finish();
                        return true;
                    } else {
                        Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    //第二种情形，第一次设置密码。
                } else if (!SaveUtils.getHasSetLock()){
                    //第一次设置密码
                    inputTV.setText("输入密码");
                    if (temp) {
                        firstPass = sb.toString();
                        lockView.resetPoints();
                        inputTV.setText("再次输入以确认");
                        temp = false;
                    } else {
                        //第二次
                        if (sb.toString().equals(firstPass)) {
                            Toast.makeText(ScreenLockActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                            SaveUtils.setPassword(firstPass);
                            SaveUtils.setLockStyle(Constant.NINEBLOCKLOCK);
                            SaveUtils.setHasSetLock(true);
                            finish();
                        } else {
                            Toast.makeText(ScreenLockActivity.this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                            temp = true;
                        }
                    }
                    //num锁模式，在加载页面时已经验证密码，这儿只用重设即可
                }else if (SaveUtils.getHasSetLock()&&SaveUtils.getLockStyle()){
                    inputTV.setText("输入密码");
                    if (temp) {
                        firstPass = sb.toString();
                        lockView.resetPoints();
                        inputTV.setText("再次输入以确认");
                        temp = false;
                    } else {
                        //第二次
                        if (sb.toString().equals(firstPass)) {
                            Toast.makeText(ScreenLockActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                            SaveUtils.setPassword(firstPass);
                            SaveUtils.setLockStyle(Constant.NINEBLOCKLOCK);
                            SaveUtils.setHasSetLock(true);
                            finish();
                        } else {
                            Toast.makeText(ScreenLockActivity.this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                            temp = true;
                        }
                    }
                    //nineblocklock模式，先验证再重设
                }else {
                    inputTV.setText("输入密码");
                    if (temp_change) {
                        if (SaveUtils.getPassword().equals(sb.toString())) {
                            temp_change = false;
                            lockView.resetPoints();
                            Toast.makeText(ScreenLockActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else {
                        inputTV.setText("重新设置密码");
                        if (temp) {
                            firstPass = sb.toString();
                            lockView.resetPoints();
                            inputTV.setText("再次输入以确认");
                            temp = false;
                        } else {
                            //第二次
                            if (sb.toString().equals(firstPass)) {
                                Toast.makeText(ScreenLockActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                                SaveUtils.setPassword(firstPass);
                                SaveUtils.setLockStyle(Constant.NINEBLOCKLOCK);
                                SaveUtils.setHasSetLock(true);
                                finish();
                            } else {
                                Toast.makeText(ScreenLockActivity.this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                                temp = true;
                            }
                        }
                    }

                }
                return true;
            }
        });
    }
}