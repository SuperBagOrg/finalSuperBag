package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private LinearLayout loginBackLL;
    private Button loginBackBT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this, "\n" +
                "\n" +
                "8021c71fc35906b67dc7a4a0f64ef5da");
        initView();
        initListener();
    }

    private void initView(){
        phoneET = (EditText)findViewById(R.id.login_phone_et);
        passwordET = (EditText)findViewById(R.id.login_password_et);
        registerBT = (Button)findViewById(R.id.login_register_bt);
        forgetBT = (Button)findViewById(R.id.login_forget_bt);
        loginBT = (Button)findViewById(R.id.login_bt);
        loginBackLL = (LinearLayout)findViewById(R.id.login_back_ll);
        loginBackBT = (Button)findViewById(R.id.login_back);
    }

    private void initListener(){
        registerBT.setOnClickListener(this);
        forgetBT.setOnClickListener(this);
        loginBT.setOnClickListener(this);
        loginBackLL.setOnClickListener(this);
        loginBackBT.setOnClickListener(this);
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
                    //先判断与本地的账号密码是否一致
                    if (LoginUtils.getPhoneNumber().equals(phoneInput)){
                        if (LoginUtils.isRightPass(passwordInput)){
//                        if (LoginUtils.isRightPass(MD5Utils.GetMD5Code(passwordInput))){
                            LoginUtils.setLoginStatus(true);
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
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
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        LoginUtils.setPassword(user.getPassword());
                                        LoginUtils.setPhoneNumber(user.getName());
                                        LoginUtils.setLoginStatus(true);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(LoginActivity.this,"请确保网络通畅并且当前账号已注册",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                break;
            case R.id.login_back_ll:
            case R.id.login_back:
                finish();
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
