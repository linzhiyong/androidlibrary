package com.lzy.androidlibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * BitmapUtil工具类
 *
 * @author linzhiyong
 * @time 2016年11月15日12:51:50
 * @email wflinzhiyong@163.com
 * @desc
 */
public class LBitmapUtil {

    private static final String TAG = LBitmapUtil.class.getName();

    /**
     * 保存Bitmap到内存中
     *
     * @param path     保存路径
     * @param fileName 文件名称
     * @param bitmap   bitmap
     */
    public static void saveBitmap(String path, String fileName, Bitmap bitmap) {
        saveBitmap(path + File.separator + fileName, bitmap);
    }

    /**
     * 保存Bitmap到内存中
     *
     * @param path     保存路径
     * @param bitmap   bitmap
     */
    public static void saveBitmap(String path, Bitmap bitmap) {
        File file = new File(path);
        FileOutputStream out = null;
        try {
            file.getParentFile().mkdirs();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            LoggerUtil.error(TAG, e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 将Bitmap对象读到字节数组中
     *
     * @param bitmap
     * @return byte[]
     */
    public static byte[] readBitmapToByte(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将字节数组转为Bitmap对象
     *
     * @param b byte[]
     * @return Bitmap
     */
    public static Bitmap transformByteToBitmap(byte[] b) {
        if (b == null) {
            return null;
        } else {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
    }

    /**
     * 把图片状态由彩色变成灰色
     *
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return faceIconGreyBitmap;
    }

}
