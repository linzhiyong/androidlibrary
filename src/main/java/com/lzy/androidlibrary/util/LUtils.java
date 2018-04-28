package com.lzy.androidlibrary.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lzy.androidlibrary.R;

import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 工具类
 *
 * @author linzhiyong
 * @time 2016年11月17日08:26:37
 * @email wflinzhiyong@163.com
 * @desc
 */
public class LUtils {

    private static long lastClickTime = 0;

    /**
     * 防止按钮连续点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

    /**
     * 防止按钮连续点击
     *
     * @param interval
     * @return
     */
    public static boolean isFastDoubleClick(long interval) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < interval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 加密明文，并转化为base64字符串
     *
     * @param context
     * @param str
     * @return
     * @throws Exception
     */
    public static String toEncryptPwd(Context context, String str) throws Exception {
        byte[] encryptPwd = des3EncodeCBC(getIntArray(context, R.array.key), getIntArray(context, R.array.keyiv), str.getBytes());
        return android.util.Base64.encodeToString(encryptPwd, android.util.Base64.DEFAULT);
    }

    /**
     * 解密android.util.Base64字符串，并转化为明文
     *
     * @param context
     * @param base64Str
     * @return
     * @throws Exception
     */
    public static String toDecryptPwd(Context context, String base64Str) throws Exception {
        byte[] base64Byte = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT);
        byte[] decryptPwd = des3DecodeCBC(getIntArray(context, R.array.key), getIntArray(context, R.array.keyiv), base64Byte);
        return new String(decryptPwd);
    }

    /**
     * 获取存储秘钥和初始向量的字节数组
     * @desc int 转 byte
     *
     * @param context
     * @param res
     * @return
     */
    public static byte[] getIntArray(Context context, int res) {
        int[] array = context.getResources().getIntArray(res);
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    /**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

        return cipher.doFinal(data);
    }
    /**
     * 解密
     * @param key 3*8位密钥
     * @param keyiv 8位密钥向量
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {

        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // 解密
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ivp = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, securekey, ivp);

        return cipher.doFinal(data);
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNum
     */
    public static void callPhone(Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL); // 进入系统拨号界面
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    /**
     * 验证手机号格式正确性
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、147(TD)、157(TD)、158、159、178、187、188
        联通：130、131、132、152、155、156、176、185、186
        电信：133、153、177、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[4578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 校验权限是否被授权
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        if (canMakeSmores()) {
            return (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private static boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * 获取手机IMEI
     *
     * android.permission.READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getDeviceId();
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumer(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
