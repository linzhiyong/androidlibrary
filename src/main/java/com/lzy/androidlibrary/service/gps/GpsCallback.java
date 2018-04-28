package com.lzy.androidlibrary.service.gps;

import android.location.Location;

/**
 * Gps回调接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/3
 * @desc
 */
public interface GpsCallback {

    /** 开始定位 */
    void onStart();

    /** 定位回调，location定位信息 */
    void onLocation(Location location);

    /** 定位停止 */
    void onStop();

    /** 定位失败 */
    void onError(Exception e);

}
