package com.lzy.androidlibrary.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * android资源获取工具类, 根据资源名、分类进行获取
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public class LResourceUtil {

    /**
     * 获取资源
     *
     * @param context
     * @param resourceName
     * @param resourceType
     * @return
     */
    public static int getResource(Context context, String resourceName, String resourceType) {
        return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
    }

    /**
     * 获取string类型资源 R.string.xxx
     *
     * @param context
     * @param resourceName
     * @param defVal
     * @return
     */
    public static String getString(Context context, String resourceName, String defVal){
        int id = getResource(context, resourceName, "string");
        if (id == 0) {
            return defVal;
        }
        return context.getString(id);
    }

    /**
     * 获取图片资源 R.drawable.xxx
     *
     * @param context
     * @param drawableName
     * @return
     */
    public static Drawable getDrawable(Context context, String drawableName) {
        int id = getResource(context, drawableName, "drawable");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return context.getResources().getDrawable(id, context.getTheme());
//        } else {
            return context.getResources().getDrawable(id);
//        }
    }

    /**
     * 获取布局资源 R.layout.xxx
     *
     * @param context
     * @param layoutName
     * @return
     */
    public static int getLayoutRes(Context context, String layoutName) {
        return getResource(context, layoutName, "layout");
    }

    /**
     * 获取布局资源 R.menu.xxx
     *
     * @param context
     * @param layoutName
     * @return
     */
    public static int getMenuRes(Context context, String layoutName) {
        return getResource(context, layoutName, "menu");
    }

    /**
     * 获取id资源 R.id.xxx
     *
     * @param context
     * @param idName
     * @return
     */
    public static int getIdRes(Context context, String idName) {
        return getResource(context, idName, "id");
    }

}
