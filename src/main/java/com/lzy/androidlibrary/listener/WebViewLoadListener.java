package com.lzy.androidlibrary.listener;

/**
 * WebView加载回调接口.
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/17
 * @desc
 */
public interface WebViewLoadListener {

    void onPageStart();

    void onPageProgress(int progress);

    void onPageError(Exception e);

    void onPageFinish();

}
