package com.xhh.frameworklib.common.utils;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.xhh.frameworklib.common.utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
	private static final String charset = "utf-8";


	/**
	 * 
	 * <p>
	 * Title: Encrypt
	 * </p>
	 * <p>
	 * Description: AES加密
	 * </p>
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param plaintext
	 *            要加密的字串
	 * @param sKey
	 *            加密的密钥
	 * @return
	 * @throws Exception
	 * 
	 * @author xwc1125
	 * @date 2015-7-1下午1:38:05
	 */
	@SuppressLint({ "TrulyRandom", "DefaultLocale" })
	public static String encrypt(String plaintext, String sKey) {
		String encryptResult = null;
		String result=null;
		if (StringUtils.isEmpty(plaintext)) {
			System.out.println("encrypt content is null");
			return null;
		}
		if (StringUtils.isEmpty(sKey)) {
			System.out.println("encrypt key is null");
			return null;
		}
		if (sKey.length() != 16) {
			System.out.println("encrypt key length error");
			return null;
		}
		try {
			byte[] raw = sKey.getBytes(charset);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(plaintext.getBytes(charset));
			result=Base64.encodeToString(encrypted,Base64.DEFAULT);
//			encryptResult=byte2hex(result.getBytes());
			encryptResult=stringToHexString(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return encryptResult;
	}

	/**
	 * 
	 * <p>
	 * Title: Decrypt
	 * </p>
	 * <p>
	 * Description: AES解密
	 * </p>
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param ciphertext
	 *            要解密的加密后字符串
	 * @param sKey
	 *            解密秘钥
	 * @return
	 * @throws Exception
	 * 
	 * @author xwc1125
	 * @date 2015-7-1下午1:37:42
	 */
	public static String decrypt(String ciphertext, String sKey) {
		String decryptResult = null;
		if (StringUtils.isEmpty(ciphertext)) {
			System.out.println("decrypt content is null");
			return decryptResult;
		}
		if (StringUtils.isEmpty(sKey)) {
			System.out.println("decrypt key is null");
			return decryptResult;
		}
		if (sKey.length() != 16) {
			System.out.println("decrypt key length error");
			return decryptResult;
		}
		try {
			byte[] raw = sKey.getBytes(charset);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(ciphertext);
			byte[] original = cipher.doFinal(encrypted1);
			decryptResult = new String(original, charset);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return decryptResult;
	}

	private static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	@SuppressLint("DefaultLocale")
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}


	/**
	 *  
	 *      * 字符串转换为16进制字符串 
	 *      *  
	 *      * @param s 
	 *      * @return 
	 *      
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str.substring(0,str.length()-1);
	}
}
