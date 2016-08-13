package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.R;
import com.example.k.superbag2.utils.LoginUtils;

/**
 * Created by K on 2016/8/13.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneET,passwordET;
    private Button registerBT,forgetBT,loginBT;
    private Button noLoginBT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    private void initView(){
        phoneET = (EditText)findViewById(R.id.login_phone_et);
        passwordET = (EditText)findViewById(R.id.login_password_et);
        registerBT = (Button)findViewById(R.id.login_register_bt);
        forgetBT = (Button)findViewById(R.id.login_forget_bt);
        loginBT = (Button)findViewById(R.id.login_bt);
        noLoginBT = (Button)findViewById(R.id.login_no_bt);
    }

    private void initListener(){
        registerBT.setOnClickListener(this);
        forgetBT.setOnClickListener(this);
        loginBT.setOnClickListener(this);
        noLoginBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_register_bt:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.login_forget_bt:

                break;
            case R.id.login_bt:
                String passwordInput = passwordET.getText().toString();
                if (passwordInput.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                } else {
                    if (LoginUtils.isRightPass(passwordInput)){
                        LoginUtils.setLoginStatus(true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.login_no_bt:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}
