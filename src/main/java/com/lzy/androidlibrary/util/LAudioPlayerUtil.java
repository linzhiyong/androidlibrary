package com.lzy.androidlibrary.util;

import android.media.MediaPlayer;

/**
 * 音频播放工具类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/31
 * @desc
 */
public class LAudioPlayerUtil {

    private static final String TAG = "AudioRecordTest";

    private MediaPlayer mPlayer;

    public LAudioPlayerUtil() {
    }

    /**
     * 开始播放
     *
     * @param mFileName 音频文件
     * @param listener 播放完成回调
     */
    public void start(String mFileName, MediaPlayer.OnCompletionListener listener) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        } else {
            mPlayer.reset();
        }

        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            if (listener != null) {
                mPlayer.setOnCompletionListener(listener);
            }
        } catch (Exception e) {
            LoggerUtil.error(TAG, "prepare() failed" + e.getMessage(), e);
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

}
