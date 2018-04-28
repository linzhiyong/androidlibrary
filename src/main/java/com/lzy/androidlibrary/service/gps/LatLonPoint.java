package com.lzy.androidlibrary.service.gps;

import java.io.Serializable;

/**
 * 类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/23
 * @desc
 */
public class LatLonPoint implements Serializable {

    /** 纬度 */
    private double latitude;

    /** 经度 */
    private double longitude;

    public LatLonPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLonPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
