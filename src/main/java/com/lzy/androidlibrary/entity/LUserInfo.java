package com.lzy.androidlibrary.entity;

import java.io.Serializable;

/**
 * LUserInfo实体类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/3
 * @desc
 */
public class LUserInfo implements Serializable {

    private int code;

    private String userId;

    private String userName;

    private String trueName;

    private String password;

    private String cookie;

    private String mobile;

    private String address;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
