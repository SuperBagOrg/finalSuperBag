package com.example.k.superbag2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.k.superbag2.MyApplication;

/**
 * Created by K on 2016/8/1.
 */
public class SaveUtils {
    //提醒时间
    public static long getAlarmTime() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("alarm",Context.MODE_PRIVATE);
        Log.d("alarm","get:"+sp.getLong("alarm",0L));
        return sp.getLong("alarm",0L);
    }
    public static void setAlarmTime(Long pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("alarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("alarm",pass);
        editor.apply();
        Log.d("alarm","set:"+pass);

    }

    public static String getPassword() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("lock_password",Context.MODE_PRIVATE);
        return sp.getString("lock_password","");
    }
    public static void setPassword(String pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("lock_password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lock_password",pass);
        editor.apply();
    }
    //更新时间
    public static float getUpdateTime() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("updateTime",Context.MODE_PRIVATE);
        return sp.getFloat("updateTime",0);
    }
    public static void setUpdateTime(float pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("updateTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("updateTime",pass);
        editor.apply();
    }
    public static String getPasswordnum() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("passwordnum",Context.MODE_PRIVATE);
        Log.d("shared","get"+sp.getString("password",""));
        return sp.getString("passwordnum","");
    }
    public static void setPasswordnum(String pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("passwordnum", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("passwordnum",pass);
        editor.apply();
        Log.d("shared","set"+pass);
    }

    //islocked
    public static void setIsLocked(boolean isLocked){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("islocked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("islocked",isLocked);
        editor.apply();
    }
    public static boolean getIsLocked(){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("islocked",Context.MODE_PRIVATE);
        return sp.getBoolean("islocked",false);
    }
    //hassetlock
    public static void setHasSetLock(boolean hassetlock){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("hassetlock", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("hassetlock",hassetlock);
        editor.apply();
    }
    public static boolean getHasSetLock(){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("hassetlock",Context.MODE_PRIVATE);
        return sp.getBoolean("hassetlock",false);
    }
    //lockstyle
    public static void setLockStyle(boolean lockstyle){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("lockstyle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("lockstyle",lockstyle);
        editor.apply();
    }
    public static boolean getLockStyle(){
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("lockstyle",Context.MODE_PRIVATE);
        return sp.getBoolean("lockstyle",false);
    }

}
