package com.example.k.superbag2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.k.superbag2.MyApplication;

/**
 * Created by K on 2016/8/13.
 */
public class LoginUtils {

    private static final String IS_LOGIN = "is_login";

    //存取缓存账号名：即手机号
    public static String getPhoneNumber() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("phone_number",Context.MODE_PRIVATE);
        return sp.getString("phone_number","");
    }
    public static void setPhoneNumber(String pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("phone_number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phone_number",pass);
        editor.apply();
    }
    //存取缓存密码
    public static boolean isRightPass(String passwordInput){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("password", Context.MODE_PRIVATE);
        String password_saved = sp.getString("password","");
        return passwordInput.equals(password_saved);
    }

    public static void setPassword(String password){
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("password", Context.MODE_PRIVATE).edit();
        editor.putString("password",password);

        editor.apply();
    }

    public static boolean getLoginStatus(){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(IS_LOGIN,Context.MODE_PRIVATE);
        return sp.getBoolean(IS_LOGIN,false);
    }

    public static void setLoginStatus(boolean isLogin){
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(IS_LOGIN,Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_LOGIN,isLogin);
        editor.apply();
    }
}
