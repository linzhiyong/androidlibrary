package com.lzy.androidlibrary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算文件MD5值工具类.
 * 
 * @author linzhiyong
 * @time 2015年11月4日10:27:29
 * @email wflinzhiyong@163.com
 * 
 */
public class LMD5Util {
    
	/** The M d5. */
	static MessageDigest MD5 = null;
	
	/** The Constant HEX_DIGITS. */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ne) {
			ne.printStackTrace();
		}
	}

	/**
	 * 获取文件md5值.
	 *
	 * @param file the file
	 * @return md5串
	 * @throws IOException 
	 */
	public static String getFileMD5String(File file) throws IOException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				MD5.update(buffer, 0, length);
			}

			return new String(encodeHex(MD5.digest()));
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	/**
	 * 获取文件md5值.
	 *
	 * @param data the byte[] data
	 * @return md5串
	 * @throws IOException 
	 */
	public static String getFileMD5String(byte[] data) throws IOException {
		MD5.update(data);
		return new String(encodeHex(MD5.digest()));
	}

	/**
	 * Encode hex.
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	public static String encodeHex(byte bytes[]) {
		return bytesToHex(bytes, 0, bytes.length);

	}

	/**
	 * Bytes to hex.
	 *
	 * @param bytes the bytes
	 * @param start the start
	 * @param end the end
	 * @return the string
	 */
	public static String bytesToHex(byte bytes[], int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + end; i++) {
			sb.append(byteToHex(bytes[i]));
		}
		return sb.toString();

	}

	/**
	 * Byte to hex.
	 *
	 * @param bt the bt
	 * @return the string
	 */
	public static String byteToHex(byte bt) {
		return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];

	}
	
	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
	
	public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		long beginTime = System.currentTimeMillis();
		File fileZIP = new File("E:/android-sdk-windows.7z");
		String md5 = "";
		try {
			md5 = getFileMD5String(fileZIP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("MD5:" + md5 + "\n time:" + ((endTime - beginTime))
				+ "ms");
	}
}
