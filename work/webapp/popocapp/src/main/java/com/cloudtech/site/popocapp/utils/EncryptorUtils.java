package com.cloudtech.site.popocapp.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * 加密类，此为可逆加密
 * 
 * @author taofucheng
 * 
 */
public class EncryptorUtils {
	private String salt;
	private StrongTextEncryptor enc = new StrongTextEncryptor();
	private StrongPasswordEncryptor passEnc = new StrongPasswordEncryptor();

	public String encode(String plainText) {
		enc.setPassword(salt);
		try {
			return Base64.encodeBase64URLSafeString(enc.encrypt(plainText).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return plainText;
		}
	}

	public String decode(String encText) {
		enc.setPassword(salt);
		try {
			return enc.decrypt(new String(Base64.decodeBase64(encText), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return encText;
		}
	}

	/**
	 * 这种加密方式，其实是SHA-256算法，即摘要算法，没有salt干扰的。
	 * 
	 * @param plainText
	 * @return
	 */
	public String encodePass(String plainText) {
		return passEnc.encryptPassword(plainText);
	}

	public boolean checkPass(String plainPassword, String encryptedPassword) {
		return passEnc.checkPassword(plainPassword, encryptedPassword);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
