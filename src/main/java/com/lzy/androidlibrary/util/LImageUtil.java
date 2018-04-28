package com.lzy.androidlibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 图片管理工具类
 * 
 * @author linzhiyong
 * @time 2015年11月2日11:41:13
 * @email wflinzhiyong@163.com
 * 
 */
public class LImageUtil {

	private static final int DEFAULT_IMAGESIZE = 600;  //默认图片压缩不超过600kb

	/**
	 * 压缩并保存图片
	 *
	 * @param sourcePath 原图片地址
	 * @param targetPath 目标地址
	 * @param targetSize 目标大小 单位kb
	 * @return
	 */
	public static boolean imageCompress(String sourcePath, String targetPath, int targetSize) {
		Bitmap sourceBitmap = BitmapFactory.decodeFile(sourcePath);
		if (sourceBitmap == null) {
			return false;
		}

		targetSize = targetSize <= 0 ? DEFAULT_IMAGESIZE : targetSize;
		sourceBitmap = imageCompress(targetSize, sourceBitmap);

		// 循环压缩
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bitmapDatas = baos.toByteArray();
		int tempSize = targetSize;
		while (bitmapDatas.length / 1024 >= targetSize) {  //循环压缩图片，保证图片压缩至规定大小
			tempSize -= 5;
			sourceBitmap = imageCompress(tempSize, sourceBitmap);
			baos = new ByteArrayOutputStream();
			sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bitmapDatas = baos.toByteArray();
		}

		// 保存图片
		LBitmapUtil.saveBitmap(targetPath, sourceBitmap);
		if (!sourceBitmap.isRecycled()) {
			sourceBitmap.recycle();
		}
		return true;
	}

	/**
	 * 图片压缩方法
	 * 
	 * 计算 bitmap大小，如果超过imageSize，则进行压缩<br>
	 * 
	 * bitmap：NullPointerException，这里抛出一个异常，调用时进行处理
	 * 
	 * @param imageSize 目标图片大小 kb
	 * @param bitmap 需要压缩的图片
	 * @return bitmap
	 */
	public static Bitmap imageCompress(int imageSize, Bitmap bitmap) {
		if (imageSize <= 0){
			imageSize = DEFAULT_IMAGESIZE;
		}
		if(bitmap == null){
			throw new NullPointerException("The bitmap cannot be null from ImageUtil ImageCompress()");
		}
		double targetwidth = Math.sqrt(imageSize * 1000);
		if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();
			// 计算宽高缩放率
			double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth / bitmap.getHeight());
			// 缩放图片动作
			matrix.postScale((float) x, (float) x);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		return bitmap;
	}
	
	/**
	 * 使用BitmapFactory压缩指定路径的图片，返回bitmap对象<br>
	 * 
	 * imgPath：NullPointerException，这里抛出一个异常，调用时进行处理
	 *  
	 * @param imgPath 图片路径 ../image.jpg
	 * @param scale int scale 压缩倍数 
	 * @return bitmap
	 * @throws FileNotFoundException
	 */
	public static Bitmap imageCompressWithFactory(String imgPath, int scale) throws FileNotFoundException{
		if(scale == 0){
			scale = 1;
		}
		if(imgPath == null){
			throw new NullPointerException("The path == null from ImageUtil ImageCompressWithFactory()");
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(imgPath)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		options.inPreferredConfig = Bitmap.Config.RGB_565; // 默认是ARGB_8888 占4字节，RGB_565占2字节
		return BitmapFactory.decodeStream(in, null, options);
	}

}
