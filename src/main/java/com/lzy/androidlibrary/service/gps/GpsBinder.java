package com.lzy.androidlibrary.service.gps;

import android.os.Binder;

/**
 * service进程间通信Binder实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/3
 * @desc
 */
public class GpsBinder extends Binder {

    private GpsService service;

    public GpsBinder(GpsService service) {
        this.service = service;
    }

    /**
     * 获取GPS位置信息
     *
     * @param interval 间隔
     * @param distance 间隔
     * @param type 定位类型
     * @param callback 定位回调
     */
    public void getLocation(int interval, int distance, int type, GpsCallback callback) {
        service.getLocation(interval, distance, type, callback);
    }

    /**
     * 是否已经开启GPS定位
     *
     * @return
     */
    public boolean isOpenGps() {
        return service.isOpenGps();
    }

    /**
     * 停止服务
     */
    public void stop() {
        service.stop();
    }

}
