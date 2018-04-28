package com.lzy.androidlibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 检查网络状态
 *
 * @author linzhiyong
 * @time 2015年6月4日11:30:11
 * @email wflinzhiyong@163.com
 *
 */

@SuppressLint("InflateParams")
public class LCheckNetWorkInfo {

	private static final String TAG = LCheckNetWorkInfo.class.getName();

	/**
	 * 读取设备网络信息，检查网络状态
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			LoggerUtil.warning(TAG, "无法获取网络状态.");
			return false;
		}

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}

		return networkInfo.isConnected();
	}

	/**
	 * 读取设备网络信息，检查网络状态
	 *
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	public static boolean checkNetworkInfo(final Context ctx) throws Exception {
		ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		// api 21
//		// mobile 3G Data Network
//		State mobile = conMan.getNetworkInfo(conMan.getActiveNetwork()).getState();
//		// wifi
//		State wifi = conMan.getNetworkInfo(conMan.getActiveNetwork()).getState();

		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return true;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;

//		final Dialog dialog = new Dialog(ctx, R.style.dialog);
//		View view = LayoutInflater.from(ctx).inflate(R.layout.popwindow, null);
//		dialog.setContentView(view);
//		TextView messageView = (TextView) view.findViewById(R.id.confirmMsg);
//		Button confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
//		Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
//		messageView.setText(ctx.getString(R.string.dialog_net));
//		confirmBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 进入无线网络配置界面
//				dialog.dismiss();
//				ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//			}
//		});
//		cancelBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.show();

		return false;
	}

	/**
	 * 获取网络连接类型
	 *
	 * @param ctx
	 * @return
	 */
	public static int getNetworkType(Context ctx) {
		ConnectivityManager connectivityManager  = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null) {
			return info.getType();
		}
		return -1;
	}

}
