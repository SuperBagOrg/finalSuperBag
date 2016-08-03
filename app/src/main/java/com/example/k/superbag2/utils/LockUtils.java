package com.example.k.superbag2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.k.superbag2.MyApplication;

/**
 * Created by K on 2016/8/1.
 */
public class LockUtils {
    public static String getPassword() {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("password",Context.MODE_PRIVATE);
        Log.d("shared","get"+sp.getString("password",""));
        return sp.getString("password","");
    }
    public static void setPassword(String pass) {
        SharedPreferences sp =
                MyApplication.getContext().getSharedPreferences("password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password",pass);
        editor.commit();
        Log.d("shared","set"+pass);
    }
}
