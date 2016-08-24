package com.example.k.superbag2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.SaveUtils;
import java.util.List;
import java.util.Map;
/**
 * Created by K on 2016/7/30.
 */
public class NumLockActivity extends AppCompatActivity {
    private Button deleteBT,cancelBT;
    private List<Map<String,Object>> itemLists;
    int[] buttons = new int[10];
    int[] dots = new int[6];
    private Button temp;
    private StringBuilder sb;
    private int count = 0;
    private int lock = 0;
    private View tempview;
    private String password;
    private boolean flag = true;
    private boolean temp_change = true;
    private Intent intent;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPre();
        initView();
        initClick();
    }

    @Override
    protected void onResume() {
        intent = getIntent();
        if (SaveUtils.getHasSetLock()&&intent.getBooleanExtra("set",false)&&!SaveUtils.getLockStyle()
                &&temp_change){
            Intent getNineBlockLock = new Intent(NumLockActivity.this,ScreenLockActivity.class);
            startActivity(getNineBlockLock);
            Log.d("change","num---------->block");
            temp_change = false;
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        intent = getIntent();
        switch (key) {
            case KeyEvent.KEYCODE_BACK: {
                if (intent.getBooleanExtra("set",false)){
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
    private void initPre(){
        intent = getIntent();
        buttons = new int[]{
                R.id.num_lock_bt0, R.id.num_lock_bt1, R.id.num_lock_bt2, R.id.num_lock_bt3,
                R.id.num_lock_bt4, R.id.num_lock_bt5, R.id.num_lock_bt6, R.id.num_lock_bt7,
                R.id.num_lock_bt8, R.id.num_lock_bt9
        };
        dots = new int[]{
                R.id.num_lock_dot1,R.id.num_lock_dot2,R.id.num_lock_dot3,
                R.id.num_lock_dot4,R.id.num_lock_dot5,R.id.num_lock_dot6
        };
    }
    private void initPassword(){
        sb = new StringBuilder("");
        count = 0;
        for (int j = 0;j<dots.length;j++){
            tempview = findViewById(dots[j]);
            tempview.setBackgroundResource(R.drawable.num_unlock_dot);

        }
    }
    private  void savePassword(){
        SaveUtils.setHasSetLock(true);
        SaveUtils.setLockStyle(Constant.NUMBERLOCK);
        SaveUtils.setPasswordnum(password);
    }

    private void initView(){
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_num_lock);
        sb = new StringBuilder();
        textView = (TextView) findViewById(R.id.num_lock_tv);
        cancelBT = (Button) findViewById(R.id.num_lock_cancel);
        deleteBT = (Button) findViewById(R.id.num_lock_delete_bt);
        for (int i = 0; i <buttons.length; i++){
            temp = (Button) findViewById(buttons[i]);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count<6) {
                        sb.append(((Button) view).getText());
                        tempview = findViewById(dots[count]);
                        tempview.setBackgroundResource(R.drawable.num_lock_dot);
                        deleteBT.setVisibility(View.VISIBLE);
                        count++;
                    }
                    if (count==6){
                        //最常见的情况，由外部跳入，直接判断即可:1、由锁屏进入。2、NineLock切换时的验证
                        if (!intent.getBooleanExtra("set",false) &&SaveUtils.getHasSetLock()){
                            if (sb.toString().equals(SaveUtils.getPasswordnum())){
                                Toast.makeText(NumLockActivity.this,"解锁成功",
                                        Toast.LENGTH_SHORT).show();
                                if (intent.getBooleanExtra("lock",false)){
                                    SaveUtils.setHasSetLock(false);
                                }
                                finish();
                            }else {
                                Toast.makeText(NumLockActivity.this,"密码错误，请重试",
                                        Toast.LENGTH_SHORT).show();
                                initPassword();
                            }
                            //次常见情况，更改密码：1、之前设置为Num模式 2、之前设置为NineLock模式
                        }else if (SaveUtils.getHasSetLock()){
                            if (SaveUtils.getLockStyle()){//Num模式:先验证，在输入一次，验证一次
                                if (lock==2&&sb.toString().equals(password)){
                                    Toast.makeText(NumLockActivity.this,"新密码设置成功",
                                            Toast.LENGTH_SHORT).show();
                                    savePassword();
                                    finish();
                                }else if (lock==2){
                                    Toast.makeText(NumLockActivity.this,"前后密码不一致，请重设",
                                            Toast.LENGTH_SHORT).show();
                                    initPassword();
                                    lock = 1;
                                    textView.setText("前后密码不一致，请重设");
                                }
                                if (lock==1){
                                    Log.d("numlock",sb.toString()+"============================2_3");
                                    password = sb.toString();
                                    textView.setText("请再次输入以确定密码");
                                    initPassword();
                                    lock++;
                                }
                                if (lock==0&&sb.toString().equals(SaveUtils.getPasswordnum())){
                                    Log.d("numlock",sb.toString()+"============================2_2");
                                    Toast.makeText(NumLockActivity.this,"密码正确",
                                            Toast.LENGTH_SHORT).show();
                                    textView.setText("重新设置密码");
                                    initPassword();
                                    lock++;
                                }else if (lock==0){
                                    Log.d("numlock",sb.toString()+"============================2_6");

                                    Toast.makeText(NumLockActivity.this,"密码错误，请重试",
                                            Toast.LENGTH_SHORT).show();
                                }



                            }else if(!SaveUtils.getLockStyle()){//NineLock模式。在进入该活动前验证。这儿只用输入两次以记录新密码。
                                Log.d("numlock",sb.toString()+"============================3_1");


                                if (!flag&&sb.toString().equals(password)){
                                    Log.d("numlock",sb.toString()+"============================3_3");

                                    Toast.makeText(NumLockActivity.this,"新密码设置成功",
                                            Toast.LENGTH_SHORT).show();
                                    savePassword();
                                    finish();
                                }else if (!flag){
                                    Log.d("numlock",sb.toString()+"============================3_4");

                                    Toast.makeText(NumLockActivity.this,"前后密码不一致，请重设",
                                            Toast.LENGTH_SHORT).show();
                                    initPassword();
                                    textView.setText("前后密码不一致，请重设");
                                    flag = true;
                                }
                                if (flag){
                                    Log.d("numlock",sb.toString()+"============================3_2");

                                    password = sb.toString();
                                    textView.setText("请再次输入以确定密码");
                                    flag = false;
                                    initPassword();
                                }
                            }
                        }else if (SaveUtils.getHasSetLock()&&intent.getBooleanExtra("lock",false)){//不设置锁时的验证
                            if (sb.toString().equals(SaveUtils.getPasswordnum())){
                                Toast.makeText(NumLockActivity.this,"解锁成功",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("numlock",sb.toString()+"============================5_1");
                                if (intent.getBooleanExtra("no_lock",false)){
                                    SaveUtils.setHasSetLock(false);
                                }
                                finish();
                            }else {
                                Log.d("numlock",sb.toString()+"============================5_2");

                                Toast.makeText(NumLockActivity.this,"密码错误，请重试",
                                        Toast.LENGTH_SHORT).show();
                                initPassword();
                            }
                        }else {//第一次进入的模式
                            Log.d("numlock",sb.toString()+"============================4_1");


                            if (!flag&&sb.toString().equals(password)){
                                Log.d("numlock",sb.toString()+"============================4_2");

                                Toast.makeText(NumLockActivity.this,"新密码设置成功",
                                        Toast.LENGTH_SHORT).show();
                                savePassword();
                                finish();
                            }else if (!flag){
                                Log.d("numlock",sb.toString()+"============================4_3");

                                Toast.makeText(NumLockActivity.this,"前后密码不一致，请重设",
                                        Toast.LENGTH_SHORT).show();
                                initPassword();
                                textView.setText("前后密码不一致，请重设");
                                flag = true;
                            }
                            if (flag){
                                Log.d("numlock",sb.toString()+"============================4_4");

                                password = sb.toString();
                                textView.setText("请再次输入以确定密码");
                                flag = false;
                                initPassword();
                            }
                        }
                    }
                }
            });
        }

    }
    private void initClick(){
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent.getBooleanExtra("set",false)){
                    finish();
                }else {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                }

            }
        });
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                for (int j = 0;j<dots.length;j++){
                    tempview = findViewById(dots[j]);
                    tempview.setBackgroundResource(R.drawable.num_unlock_dot);
                }
                deleteBT.setVisibility(View.INVISIBLE);
            }
        });
    }
}
