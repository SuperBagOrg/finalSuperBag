/**
 * 
 */
package com.example.k.superbag2.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author Acoe
 * @date 2016-1-15
 * @version V1.0.0
 */
public class SystemUtils {
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getPhoneHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕高度
	 * @param activity
	 * @return
	 */
	public static int getPhoneHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 屏幕高度
	}
}
