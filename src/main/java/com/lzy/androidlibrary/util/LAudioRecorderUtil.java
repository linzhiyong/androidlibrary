package com.lzy.androidlibrary.util;

import android.media.MediaRecorder;
import android.os.Handler;

import java.io.File;
import java.io.IOException;

import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;

/**
 * 录制音频工具类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/29
 * @desc
 */
public class LAudioRecorderUtil {

    private final String TAG = LAudioRecorderUtil.class.getName();

    /** 默认最大录制时间60s */
    public static final int MAX_LENGTH = 60 * 1000;

    /** 文件保存路径 */
    private String filePath;

    /** 文件夹路径 */
    private String folderPath;

    /** MediaRecorder对象 */
    private MediaRecorder mMediaRecorder;

    /** 录音时间 */
    private int maxLength;

    /** 开始时间 */
    private long startTime;
    /** 结束时间 */
    private long endTime;

    /** 录音动作回调接口 */
    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public LAudioRecorderUtil(String folderPath) {
        File path = new File(folderPath);
        if (!path.exists()) {
            path.mkdirs();
        }

        this.folderPath = folderPath;
        this.maxLength = MAX_LENGTH;
    }

    /**
     * 开始录音 使用amr格式
     *
     * @return
     */
    public void start() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
            } catch (Exception e) {
                mMediaRecorder.reset();
            }
        }
        mHandler.removeCallbacks(mUpdateMicStatusTimer);
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = folderPath + File.separator + System.currentTimeMillis() + ".amr";
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(maxLength);
            mMediaRecorder.prepare();

            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        stop();
                    }
                }
            });

            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.

            /* 获取开始时间* */
            startTime = System.currentTimeMillis();

            updateMicStatus();

            if (audioStatusUpdateListener != null) {
                audioStatusUpdateListener.onStart();
            }
        } catch (IllegalStateException e) {
            LoggerUtil.error(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage(), e);
            if (audioStatusUpdateListener != null) {
                audioStatusUpdateListener.onError(e);
            }
            cancel();
        } catch (IOException e) {
            LoggerUtil.error(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage(), e);
            if (audioStatusUpdateListener != null) {
                audioStatusUpdateListener.onError(e);
            }
            cancel();
        }
    }

    /**
     * 获取当前录音时间
     * @return
     */
    public long getSumTime() {
        return startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
    }

    /**
     * 停止录音
     */
    public long stop() {
        if (mMediaRecorder == null) {
            return 0L;
        }

        endTime = System.currentTimeMillis();

        // 在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况，捕获异常清理一下！
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            if (audioStatusUpdateListener != null) {
                audioStatusUpdateListener.onStop(filePath);
            }
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            if (audioStatusUpdateListener != null) {
                audioStatusUpdateListener.onError(e);
            }
        }
        filePath = "";
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancel() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        filePath = "";

        if (audioStatusUpdateListener != null) {
            audioStatusUpdateListener.onCancel();
        }
    }

    /**
     * 设置最大录制时间
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onProgress(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    /**
     * 设置录音监听器
     * @param audioStatusUpdateListener
     */
    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    public interface OnAudioStatusUpdateListener {

        /**
         * 开始录音
         */
        public void onStart();

        /**
         * 录音中...
         * @param db 当前声音分贝
         * @param time 录音时长
         */
        public void onProgress(double db, long time);

        /**
         * 录音失败
         * @param e
         */
        public void onError(Exception e);

        /**
         * 录音取消
         */
        public void onCancel();

        /**
         * 停止录音
         * @param filePath 保存路径
         */
        public void onStop(String filePath);
    }

}
