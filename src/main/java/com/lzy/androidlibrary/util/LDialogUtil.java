package com.lzy.androidlibrary.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lzy.androidlibrary.R;

/**
 * Dialog实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public class LDialogUtil {

    public interface DialogCallback {
        void onConfirm();
        void onCancel();
    }

    /**
     * 无作为提示框
     *
     * @param context
     * @param message
     */
    public static void information(Context context, String message) {
        createDialog(context, context.getString(R.string.l_dialog_title), message);
    }

    /**
     * 无作为警告框
     *
     * @param context
     * @param message
     */
    public static void warning(Context context, String message) {
        createDialog(context, context.getString(R.string.l_dialog_title_warning), message);
    }

    /**
     * 无作为错误框
     *
     * @param context
     * @param message
     */
    public static void error(Context context, String message) {
        createDialog(context, context.getString(R.string.l_dialog_title_error), message);
    }

    /**
     * 带确定取消按钮提示框
     *
     * @param context
     * @param message
     * @param callback
     */
    public static void request(Context context, String message, final DialogCallback callback) {
        request(context, context.getString(R.string.l_dialog_title), message, callback);
    }

    /**
     * 带确定取消按钮提示框
     *
     * @param context
     * @param title
     * @param message
     * @param callback
     */
    public static void request(Context context, String title, String message, final DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.l_dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onConfirm();
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.l_dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onCancel();
                }
            }
        });
        builder.create().show();
    }

    private static void createDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.l_dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
