package com.lzy.androidlibrary.crash;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.lzy.androidlibrary.util.LoggerUtil;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author linzhiyong
 * @time 2016-09-28 08:52:41
 * @email wflinzhiyong@163.com
 */
public class LCrashHandler implements Thread.UncaughtExceptionHandler {


    /** the CrashHandler */
    private static LCrashHandler instance = null;

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context context;

    private LCrashHandler() {
    }

    /**
     * 返回实例
     *
     * @return
     */
    public static LCrashHandler getInstance() {
        if (instance == null) {
            instance =  new LCrashHandler();
        }
        return instance;
    }

    /**
     *  初始化
     */
    public void init(Context context) {
        this.context = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LoggerUtil.error(LCrashHandler.class.getName(), "程序异常退出：" + throwable.getMessage(), throwable);

        if (!handleException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        }
        else {
            //退出程序
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
	
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context.getApplicationContext(), "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
	
}
