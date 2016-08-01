package com.example.k.superbag2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.utils.MD5;
import com.example.k.superbag2.view.LockView;
import java.util.List;

/**
 * Created by Aersasi on 2016/7/30.
 */
public class ScreenLockActivity extends AppCompatActivity {

    private Button set_password;
    private Button clean_password;
    private String password;
    private boolean isSet = false;
    private MD5 md5;
    private StringBuilder sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock);
        initview();
        initUtils();
    }
    private void initUtils(){
        md5 = new MD5();
    }
    private void initview(){
        set_password = (Button) findViewById(R.id.set_password);
        clean_password = (Button) findViewById(R.id.clean_password);
        LockView lockView = (LockView) findViewById(R.id.lockView);
        lockView.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {

            @Override
            public boolean onDrawFinished(List<Integer> passPositions) {
                sb = new StringBuilder();
                for (Integer i :
                        passPositions) {
                    sb.append(i.intValue());
                }
                if (isSet) {
                    // 比较路径是否一致
                    //sb.toString:输入的数据； md5.GetMD5Code(sb.toString())：加密后的输入数据
                    boolean valid = compare(password, md5.GetMD5Code(sb.toString()));
                    if (valid) {
                        Toast.makeText(ScreenLockActivity.this, "手势密码正确！", Toast.LENGTH_SHORT).show();
                        MyApplication.isLocked = false;
                        ScreenLockActivity.this.finish();
                    } else {
                        Toast.makeText(ScreenLockActivity.this, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                    return valid;
                } else {
                        password = md5.GetMD5Code(sb.toString());
                    return true;
                }

            }
        });
        set_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScreenLockActivity.this, "密码设置完毕为："+password, Toast.LENGTH_SHORT).show();

                if (sb.toString()!=null){
                    isSet = true;
                }

            }
        });
        clean_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSet = false;
                Toast.makeText(ScreenLockActivity.this, "密码已清除", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean compare(String password,String input){
        if (password.equals(input)){
            return true;
        } else {
            return false;
        }

    }

}