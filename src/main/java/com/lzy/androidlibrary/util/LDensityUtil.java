package com.lzy.androidlibrary.util;

import android.content.Context;

/**
 * dp、px转换工具类
 * 
 * @author linzhiyong
 * @time 2015年11月10日13:45:53
 * @email wflinzhiyong@163.com
 *
 */

public class LDensityUtil {
	
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *  
     * @param context  Context
     * @param dpValue float dpValue
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     *   
     * @param context Context
     * @param pxValue float pxValue
     * @return dp
     */
    public static int px2dp(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
}
