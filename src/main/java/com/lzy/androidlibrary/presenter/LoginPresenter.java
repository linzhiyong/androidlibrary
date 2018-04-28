package com.lzy.androidlibrary.presenter;

/**
 * MVP-Presenter登录接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/3
 * @desc
 */
public interface LoginPresenter {

    /**
     * 登录
     */
    public void login();

    /**
     * 检查自动登录
     */
    public void autoLogin();

}
