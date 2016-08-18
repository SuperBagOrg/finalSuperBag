package com.example.k.superbag2.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.k.superbag2.R;
import com.example.k.superbag2.utils.SystemUtils;

/**
 * Created by Aersasi on 2016/8/18.
 */
public class TypeDialog extends Dialog {
    public TypeDialog(Context context, View view) {
        super(context, R.style.Dialog);
        setContentView(view);
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.AnimBottom); //设置窗口弹出动画
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.gravity = Gravity.TOP;
        wl.y =230;
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 1.0f;
        window.setAttributes(wl);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    public TypeDialog(Context context, View view, int height) {
        super(context, R.style.Dialog);
        setContentView(view);

        Window  window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.AnimBottom); //设置窗口弹出动画
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.gravity = Gravity.BOTTOM;
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (height < 0 || height >= SystemUtils.getPhoneHeight(context)) {
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            wl.height = height;
        }
        wl.alpha = 1.0f;
        window.setAttributes(wl);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }
}
