package com.znb.java.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-05-05 下午8:26
 */
public class SecurityUtil {
    private static final char[] hex = "0123456789abcdef".toCharArray();

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(hex[(b & 0xFF) >> 4]).append(hex[(b & 0xF)]);
        }
        return sb.toString();
    }

    public static String MD5_32(String passwd, String charset) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        try {
            byte[] bytes = passwd.getBytes(charset);
            md5.update(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte[] digest = md5.digest();
        return bytes2Hex(digest);
    }
}
