package com.lzy.androidlibrary.entity;

import java.io.Serializable;

/**
 * 版本信息实体类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public class LAppVersionInfo implements Serializable {

    private int versionCode;

    private String versionName;

    private String desc;

    private boolean needUpdate;

    private String downloadUrl;

    public LAppVersionInfo() {
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
