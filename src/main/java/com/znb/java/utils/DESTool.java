package com.znb.java.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 *
 * DES加密解密工具
 */

public class DESTool {

	public static String encrypt(String data, String key){
		return new String(Base64.encodeBase64(encrypt(data.getBytes(),key.getBytes())));
	}

	public static String decrypt(String data, String key){
		return new String(decrypt(Base64.decodeBase64(data.getBytes()),key.getBytes()));
	}


	/**
	 * 加密函数
	 *
	 * @param data 加密数据
	 * @param key 密钥
	 * @return 返回加密后的数据
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {

		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
			byte encryptedData[] = cipher.doFinal(data);
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密函数
	 *
	 * @param data 解密数据
	 * @param key 密钥
	 * @return 返回解密后的数据
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
			byte decryptedData[] = cipher.doFinal(data);
			return decryptedData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
