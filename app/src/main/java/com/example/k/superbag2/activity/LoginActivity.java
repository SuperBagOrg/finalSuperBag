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
import com.example.k.superbag2.bean.User;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.LoginUtils;
import com.example.k.superbag2.utils.MD5Utils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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

        Bmob.initialize(this, "\n" +
                "\n" +
                "f99570680fbe559ad4c18e8dbd01382d");
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
                startActivity(new Intent(LoginActivity.this,ReSetPasswordActivity.class));

                break;
            case R.id.login_bt:
                final String passwordInput = passwordET.getText().toString();
                String phoneInput = phoneET.getText().toString();
                if (passwordInput.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                } else {
                    //先判断与本地的账号密码是否一致
                    if (LoginUtils.getPhoneNumber().equals(phoneInput)){
                        if (LoginUtils.isRightPass(passwordInput)){
//                        if (LoginUtils.isRightPass(MD5Utils.GetMD5Code(passwordInput))){
                            LoginUtils.setLoginStatus(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"密码错误 本地",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        //判断与网络上数据库里存储的账号密码是否一致
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo("name", phoneInput);
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e==null){
                                    User user = list.get(0);
//                                    if (user.getPassword().equals(MD5Utils.GetMD5Code(passwordInput))){
                                    if (user.getPassword().equals(passwordInput)){
                                        //在本地存储当前密码
                                        LoginUtils.setPassword(user.getPassword());
                                        LoginUtils.setPhoneNumber(user.getName());
                                        LoginUtils.setLoginStatus(true);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }else {
                                    Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                break;
            case R.id.login_no_bt:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getBooleanExtra(Constant.QUIT_LOGIN,false)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra(Constant.QUIT_LOGIN,true);
            startActivity(intent);
        }
    }
}
