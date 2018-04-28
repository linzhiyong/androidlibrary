package com.lzy.androidlibrary.presenter.view;

/**
 * MVP-View登录视图接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/3
 * @desc
 */
public interface LoginView {

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public String getIMEI();

    public boolean isRememberUsername();

    public void setRememberUsername(boolean remember);

    public boolean isRememberPassword();

    public void setRememberPassword(boolean remember);

    public void onLoginSuccess();

    public void onLoginError(Exception e);

    public void showLoading();

    public void hideLoading();

    public void showToast(String message);

}
