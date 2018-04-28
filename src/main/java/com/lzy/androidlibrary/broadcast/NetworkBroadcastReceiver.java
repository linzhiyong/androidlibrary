package com.lzy.androidlibrary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lzy.androidlibrary.listener.NetworkListener;
import com.lzy.androidlibrary.util.LCheckNetWorkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络广播接收器实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/21
 * @desc
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private List<NetworkListener> listeners;

    public NetworkBroadcastReceiver() {
        this.listeners = new ArrayList<NetworkListener>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (LCheckNetWorkInfo.isNetworkAvailable(context)) {
                for (NetworkListener listener : listeners) {
                    listener.onConnected();
                }
            } else {
                for (NetworkListener listener : listeners) {
                    listener.onDisConnected();
                }
            }
        }
    }

    /**
     * 添加监听器
     *
     * @param listener
     */
    public void addListener(NetworkListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移出监听器
     *
     * @param listener
     */
    public void removeListener(NetworkListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

}
