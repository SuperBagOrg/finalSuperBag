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
import com.example.k.superbag2.utils.MD5Utils;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by K on 2016/8/13.
 */
public class ReSetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneNumET,passwordET,vCodeET;
    private Button getVCodeBT,okBT,backBT;
    private LinearLayout backLL;

    private String phoneNum,verCodeGet,verCodeInput = "";
    private String password = "";
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_resetpassword);

        initView();
        initListener();
        BmobSMS.initialize(this,"f99570680fbe559ad4c18e8dbd01382d");
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
                if (!isMobileNO(phoneNum)){
                    Toast.makeText(ReSetPasswordActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else if (password.equals("")){
                    Toast.makeText(ReSetPasswordActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                } else {
                    BmobSMS.requestSMSCode(ReSetPasswordActivity.this, phoneNum, "获取验证码",
                            new RequestSMSCodeListener() {
                                @Override
                                public void done(Integer integer, BmobException e) {
                                    Log.d("点击了","33333");

                                    if (e == null) {
                                        verCodeGet = integer + "";
                                        Log.d("点击了","获取验证码");

                                    }
                                }
                            });
                    getVCodeBT.setEnabled(false);
                }
                //获取当前手机号对应的id。用以下面更新。
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("name",phoneNum);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, cn.bmob.v3.exception.BmobException e) {
                        if (e == null && !list.get(0).getName().equals(null)){
                            id = list.get(0).getObjectId();
                        }else {

                        }
                    }
                });
                break;

            case R.id.register_ok:
                phoneNum = phoneNumET.getText().toString();
                password = passwordET.getText().toString();
                BmobSMS.verifySmsCode(ReSetPasswordActivity.this, phoneNum, vCodeET.getText().toString(),
                        new VerifySMSCodeListener() {
                            private User user;
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    //在user表中更新手机号，与密码项。
                                    user = new User();
                                    user.setName(phoneNum);
//                                    user.setPassword(MD5Utils.GetMD5Code(password));
                                    user.setPassword(password);
                                    //id
                                    user.update(id, new UpdateListener() {
                                        @Override
                                        public void done(cn.bmob.v3.exception.BmobException e) {
                                            if (e==null){
                                                Log.d("register","successful");
                                                LoginUtils.setPassword(user.getPassword());
                                                LoginUtils.setPhoneNumber(user.getName());
                                                LoginUtils.setLoginStatus(true);
                                                Toast.makeText(ReSetPasswordActivity.this,"修改成功",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ReSetPasswordActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                user.save();
                                            }
                                        }
                                    });
                                } else {
                                    getVCodeBT.setEnabled(true);
                                    Toast.makeText(ReSetPasswordActivity.this,"验证码输入错误，请重试",Toast.LENGTH_SHORT).show();
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
