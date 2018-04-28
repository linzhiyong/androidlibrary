package com.lzy.androidlibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 封装Toast的工具类
 * 
 * @author linzhiyong
 * @time 2015年6月4日11:31:03
 * @email wflinzhiyong@163.com
 * 
 */

public class LToastUtil {

	/**
	 * 普通Toast显示-短时间显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showShortToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 普通Toast显示-长时间显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showLongToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 通用Toast信息显示面板-自定义位置-center
	 * 
	 * @param text
	 *            信息内容
	 */
	public static void showCenterToast(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
