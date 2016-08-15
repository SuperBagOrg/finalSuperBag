package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.User;
import com.example.k.superbag2.utils.LoginUtils;
import com.example.k.superbag2.utils.SaveUtils;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by K on 2016/8/13.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneNumET,passwordET,vCodeET;
    private Button getVCodeBT,okBT,backBT;
    private LinearLayout backLL;

    private String phoneNum,verCodeGet,verCodeInput = "";
    private String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();
        initListener();
        BmobSMS.initialize(this,"0d5d5ee39c0f5cb5c525213c1d5ee9f4");
    }

    private void initView(){
        phoneNumET = (EditText)findViewById(R.id.register_phone_et);
        passwordET = (EditText)findViewById(R.id.register_pass_et);
        vCodeET = (EditText)findViewById(R.id.register_verification_code);
        getVCodeBT = (Button)findViewById(R.id.register_get_code);
        okBT = (Button)findViewById(R.id.register_ok);
        backBT = (Button)findViewById(R.id.register_back);
        backLL = (LinearLayout)findViewById(R.id.register_back_ll);
    }

    private void initListener(){
        getVCodeBT.setOnClickListener(this);
        okBT.setOnClickListener(this);
        backBT.setOnClickListener(this);
        backLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_get_code:
                Log.d("点击了","注册按钮");
                phoneNum = phoneNumET.getText().toString();
                verCodeInput = vCodeET.getText().toString();
                password = passwordET.getText().toString();
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("name",phoneNum);
                //先检测网络数据库中是否有存在的phone，若有则不能注册
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, cn.bmob.v3.exception.BmobException e) {
                        if (e == null){
                            Toast.makeText(RegisterActivity.this,"当前账号已经注册",Toast.LENGTH_SHORT).show();
                        }else {
                            if (!isMobileNO(phoneNum)){
                                Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                            }else if (password.equals("")){
                                Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                    }
                });
                BmobSMS.requestSMSCode(RegisterActivity.this, phoneNum, "获取验证码",
                        new RequestSMSCodeListener() {
                            @Override
                            public void done(Integer integer, BmobException e) {
                                if (e == null) {
                                    verCodeGet = integer + "";
                                }
                            }
                        });
                getVCodeBT.setEnabled(false);
                break;

            case R.id.register_ok:
                phoneNum = phoneNumET.getText().toString();
                password = passwordET.getText().toString();
                BmobSMS.verifySmsCode(RegisterActivity.this, phoneNum, vCodeET.getText().toString(),
                        new VerifySMSCodeListener() {
                            private User user;
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    //在user表中添加手机号，与密码项。
                                    user = new User();
                                    user.setName(phoneNum);
                                    user.setPassword(password);
                                    user.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, cn.bmob.v3.exception.BmobException e) {
                                            if (e==null){
                                                Log.d("register","successful");
                                                LoginUtils.setPassword(user.getPassword());
                                                LoginUtils.setPhoneNumber(user.getName());
                                                Toast.makeText(RegisterActivity.this,"注册成功",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Log.d("register","unsuccessful");

                                            }
                                        }
                                    });
                                } else {
                                    getVCodeBT.setEnabled(true);
                                    Toast.makeText(RegisterActivity.this,"验证码输入错误，请重试",Toast.LENGTH_SHORT).show();
                                    Log.d("register",e.getErrorCode()+" --  "+e.getLocalizedMessage());
                                }
                            }
                        });
                break;
            case R.id.register_back:
            case R.id.register_back_ll:
                finish();
        }
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        if(mobiles==null){
            Log.e("AppUtils:", "传入号码为空");
            return false;
        }
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    虚拟运营商：170
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[07])[0-9]{8}$";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
}
