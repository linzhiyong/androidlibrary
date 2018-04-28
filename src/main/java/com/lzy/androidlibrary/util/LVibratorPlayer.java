package com.lzy.androidlibrary.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * 震动功能工具类
 *
 * @author linzhiyong
 * @time 2017-01-16 10:11:16
 */
public class LVibratorPlayer {

    private Vibrator vibrator;

    public LVibratorPlayer(Context context) {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 开始震动
     */
    public void play(long delay, long interval, boolean isRepeat) {
        long[] pattern = {delay, interval};
        this.vibrator.vibrate(pattern, isRepeat ? 0 : -1);
    }

    /**
     * 停止震动
     */
    public void stop() {
        this.vibrator.cancel();
    }

}
