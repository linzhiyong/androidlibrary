
package com.lzy.androidlibrary.util;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地缓存应用参数
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/3
 * @desc
 */
public class LSharedPreferencesUtil {

    private static final int MODE_PRIVATE = 0;
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;

    private static LSharedPreferencesUtil instance;

    private LSharedPreferencesUtil() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static LSharedPreferencesUtil getInstance() {
        if (instance == null) {
            instance = new LSharedPreferencesUtil();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        setting = context.getSharedPreferences("setting", MODE_PRIVATE);
        editor = setting.edit();
    }

    /**
     * 设置boolen类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setBoolen(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取boolean参数的value
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key, boolean def) {
        return setting.getBoolean(key, def);

    }

    public void setSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.commit();
    }

    /**
     * 设置int类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取int参数的value
     *
     * @param key
     * @return
     */
    public int getInt(String key, int def) {
        return setting.getInt(key, def);
    }

    /**
     * 设置String类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取String参数的value
     *
     * @param key
     * @return
     */
    public String getString(String key, String def) {
        return setting.getString(key, def);
    }

    /**
     * 设置float类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取float参数的value
     *
     * @param key
     * @return
     */
    public float getFloat(String key, float def) {
        return setting.getFloat(key, def);
    }

    /**
     * 设置long类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取long参数的value
     *
     * @param key
     * @return
     */
    public long getLong(String key, long def) {
        return setting.getLong(key, def);
    }

    /**
     * 设置Set<String>类型的全局参数
     *
     * @param key
     * @param value
     */
    public void setStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * 根据key值获取Set<String>参数的value
     *
     * @param key
     * @return
     */
    public Set<String> getStringSet(String key, Set<String> def) {
        return setting.getStringSet(key, def);
    }

    /**
     * 清除缓存数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

}
