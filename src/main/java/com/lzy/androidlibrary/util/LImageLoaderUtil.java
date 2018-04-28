package com.lzy.androidlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.lzy.androidlibrary.R;
import com.lzy.androidlibrary.manager.LThreadPoolManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * ImageLoader工具类
 *
 * @author linzhiyong
 * @time 2016年11月15日12:51:50
 * @email wflinzhiyong@163.com
 * @desc
 */
public class LImageLoaderUtil {

	private static LImageLoaderUtil instance = null;
	private static DisplayImageOptions options;
	private static ImageLoaderConfiguration config;

	private LImageLoaderUtil() {
	}

	public static LImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new LImageLoaderUtil();
		}
		return instance;
	}

	/**
	 * 初始化ImageLoader
	 *
	 * @param context
     */
	public void init(@NonNull Context context, String cachePath, @DrawableRes int[] resId) {
		options = new DisplayImageOptions.Builder()
				.showStubImage((resId != null && resId.length > 0) ? resId[0] : R.mipmap.ic_image_default)  //设置图片在下载期间显示的图片
				.showImageForEmptyUri((resId != null && resId.length > 1) ? resId[1] : R.mipmap.ic_image_fail)  //设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail((resId != null && resId.length > 1) ? resId[1] : R.mipmap.ic_image_fail)  // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
				.taskExecutor(LThreadPoolManager.getInstance().getExecutorService())
				.memoryCacheSize(10 * 1024 * 1024)
				.memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheSize(50 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(new File(cachePath))) // 自定义缓存路径
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.discCacheFileCount(100)// 缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.build();
		//Initialize ImageLoader with configuration
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 加载显示图片
	 *
	 * @param uri
	 * @param imgView
     */
	public static void displayImage(String uri, ImageView imgView) {
		displayImage(uri, imgView, null);
	}

	/**
	 * 加载显示图片
	 *
	 * @param uri
	 * @param imgView
	 * @param listener
     */
	public static void displayImage(String uri, ImageView imgView, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imgView, options, listener);
	}

}
