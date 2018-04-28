package com.lzy.androidlibrary.presenter.view;

/**
 * MVP-View注册视图接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/3
 * @desc
 */
public interface LRegisterView {

    public String getUsername();

    public String getPassword();

    public void onRegisterSuccess();

    public void onRegisterError(Exception e);

    public void showLoading();

    public void hideLoading();

    public void showToast(String message);

}
