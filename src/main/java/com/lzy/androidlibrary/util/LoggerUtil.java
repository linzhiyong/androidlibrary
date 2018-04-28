package com.lzy.androidlibrary.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 日志管理工具类
 *
 * @author linzhiyong
 * @time 2016-09-27 16:19:57
 * @desc
 */
public class LoggerUtil {

    private static final String TAG = LoggerUtil.class.getName();

    /** log文件夹名称 */
    private static final String LOG = "log";

    /** log文件名称 */
    private static final String LOG_NAME = "log.log";

    /** debug模式下控制台打印日志 */
    public boolean debug = false;

    /** the logger */
    private static Logger logger = null;

    /** the log file */
    private static File file = null;

    /** the LoggerTool */
    private static LoggerUtil instance = null;

    private LoggerUtil() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static LoggerUtil getInstance() {
        if (instance == null) {
            instance = new LoggerUtil();
        }
        return instance;
    }

    /**
     * 初始化Log, 生成log文件
     * 如果使用Log保存信息, 必须初始化
     *
     * @param logPath
     * @param _debug
     */
    public void init(@NonNull String logPath, boolean _debug) {
        logger = Logger.getLogger(LOG_NAME);
        logger.setLevel(Level.ALL);

        file = new File(logPath + File.separator + LOG_NAME);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        // 设置不使用parent的消息处理handler, 主要用于屏蔽向控制台输出日志
        this.debug = _debug;
        if (debug) {
            logger.setUseParentHandlers(true);
        } else {
            logger.setUseParentHandlers(false);
        }
    }

    /**
     * 记录错误日志
     *
     * @param className
     * @param msg
     */
    public static void error(String className, String msg) {
        log(Level.SEVERE, className, msg, null);
    }

    /**
     * 记录错误日志
     *
     * @param className
     * @param msg
     * @param e
     */
    public static void error(String className, String msg, Throwable e) {
        log(Level.SEVERE, className, msg, e);
    }

    /**
     * 记录警告日志
     *
     * @param className
     * @param msg
     */
    public static void warning(String className, String msg) {
        log(Level.WARNING, className, msg, null);
    }

    /**
     * 记录警告日志
     *
     * @param className
     * @param msg
     */
    public static void warning(String className, String msg, Throwable e) {
        log(Level.WARNING, className, msg, e);
    }

    /**
     * 记录配置日志
     *
     * @param className
     * @param msg
     */
    public static void config(String className, String msg) {
        log(Level.CONFIG, className, msg, null);
    }

    /**
     * 记录配置日志
     *
     * @param className
     * @param msg
     */
    public static void config(String className, String msg, Throwable e) {
        log(Level.CONFIG, className, msg, e);
    }

    /**
     * 记录日志
     *
     * @param level
     * @param className
     * @param msg
     */
    public static void log(Level level, String className, String msg, Throwable tr) {
        if (logger == null) {
            Log.e(TAG, "请调用init方法初始化！");
            return;
        }
        if (level == null) {
            level = Level.ALL;
        }

        msg += Log.getStackTraceString(tr);

        try {
            FileHandler fh = new FileHandler(file.toString(), true);
            fh.setFormatter(new SimpleFormatter());

            logger.addHandler(fh);
            logger.log(level, className + ": " + msg + "\n");
            fh.close();
            logger.removeHandler(fh);
        } catch (Exception e) {
            Log.e(className + "", msg + "", e);
        }
    }

}
