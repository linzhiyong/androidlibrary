package com.lzy.androidlibrary.util;

import android.text.TextUtils;

/**
 * 判断字符串是否为空
 * 
 * @author linzhiyong
 * @time 2015年6月4日11:30:57
 * @email wflinzhiyong@163.com
 * 
 */

public class LTextUtil {

	/**
	 * 当str不为 null 、"" 、"null" 时 返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (!TextUtils.isEmpty(str) && !str.equals("null")) {
			return false;
		}
		return true;
	}
	
}
