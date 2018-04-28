package com.lzy.androidlibrary.service.gps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Iterator;

/**
 * 获取手机GPS位置服务
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/3
 * @desc
 */
public class GpsService extends Service implements LocationListener {

    private static final String TAG = GpsService.class.getName();

    private static final int DEFAULT_INTERVAL = 10 * 1000;
    private static final int DEFAULT_DISTANCE = 10;

    public static final int NETWORK_PROVIDER = 0;
    public static final int GPS_PROVIDER = 1;

    /** 位置管理器 */
    private LocationManager locationManager;

    private GpsCallback gpsCallback;

    private GpsBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new GpsBinder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 是否已经开启GPS定位
     *
     * @return
     */
    public boolean isOpenGps() {
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
        interval = interval <= 0 ? DEFAULT_INTERVAL : interval;
        distance = distance <= 0 ? DEFAULT_DISTANCE : distance;
        gpsCallback = callback == null ? defaultCallback : callback;

        if (locationManager == null ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            gpsCallback.onError(new Exception("定位权限失败，无法获取定位数据！"));
            return;
        }

        if (type == GPS_PROVIDER && !isOpenGps()) {
            gpsCallback.onError(new Exception("手机未开启GPS设置！"));
            return;
        }

        locationManager.addGpsStatusListener(listener);
        if (type == GPS_PROVIDER) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, distance, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, distance, this);
        }
        gpsCallback.onStart();
    }

    /**
     * 停止服务
     */
    public void stop() {
        gpsCallback.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager.removeGpsStatusListener(listener);
        }
        stopSelf();
    }

    /**
     * 状态监听
     */
    private GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    // 获取当前状态
                    if (ActivityCompat.checkSelfPermission(GpsService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count= 0;
                    StringBuffer buffer = new StringBuffer();
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        // //卫星的方位角，浮点型数据
                        // System.out.println("卫星的方位角，浮点型数据"+s.getAzimuth());
                        // //卫星的高度，浮点型数据
                        // System.out.println(""+s.getElevation());
                        // 卫星的伪随机噪声码，整形数据
                        System.out.println("----卫星的伪随机噪声码，整形数据  :" + s.getPrn());
                        // 卫星的信噪比，浮点型数据, > 实测20 可以定位（35）
                        System.out.println("----卫星的信噪比，浮点型数据 : " + s.getSnr());
                        // //卫星是否有年历表，布尔型数据
                        // System.out.println(s.hasAlmanac());
                        // //卫星是否有星历表，布尔型数据
                        // System.out.println(s.hasEphemeris());
                        // //卫星是否被用于近期的GPS修正计算
                        // System.out.println(s.hasAlmanac());
                        count++;
                        buffer.append("\n");
                        buffer.append("卫星的伪随机噪声码，整形数据  :" + s.getPrn());
                        buffer.append("\n");
                        buffer.append("卫星的信噪比，浮点型数据 : " + s.getSnr());
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        }
    };

    /******************************** 定位回调 START ******************************************/

    /**
     * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            gpsCallback.onLocation(location);
        }
    }

    /**
     * Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Provider被enable时触发此函数，比如GPS被打开
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Provider被disable时触发此函数，比如GPS被关闭
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    /******************************** 定位回调 END ******************************************/

    private GpsCallback defaultCallback = new GpsCallback() {
        @Override
        public void onStart() {
        }

        @Override
        public void onLocation(Location location) {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onError(Exception e) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        gpsCallback.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager.removeGpsStatusListener(listener);
        }
    }
}
