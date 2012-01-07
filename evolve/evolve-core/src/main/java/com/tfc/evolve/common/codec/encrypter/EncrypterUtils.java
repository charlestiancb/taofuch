/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.tfc.evolve.common.codec.EncryptException;

/**
 * 加密服务工具类
 * 
 * @author taofucheng
 */
public class EncrypterUtils {
    /**
     * 用指定的算法对给定的字节码数组进行加密，并返回加密后的字节码数组。
     * 
     * @param message 预加密的字节码数组
     * @param type 加密算法
     * @return 返回加密后的字节码数组
     * @throws EncryptException 当加密发生错误时抛出该异常
     */
    public static byte[] encrypt(byte[] message, Encrypter.ENCRYPTER_TYPE type) throws EncryptException {
        return EncrypterFactory.getInstance().getEncrypter(type).encrypt(message);
    }

    /**
     * 用指定的算法对给定的字节码数组进行解密，并返回解密后的字节码数组。
     * 
     * @param encryptedMessage 预解密的字节码数组
     * @param type 加密算法
     * @return 返回解密后的字节码数组
     * @throws EncryptException 当加密发生错误时抛出该异常
     */
    public static byte[] decrypt(byte[] encryptedMessage, Encrypter.ENCRYPTER_TYPE type) throws EncryptException {
        return EncrypterFactory.getInstance().getEncrypter(type).decrypt(encryptedMessage);
    }

    /**
     * 用指定的算法对给定的字符串进行加密，并返回加密后的字符串。
     * 
     * @param message 预加密的字符串
     * @param type 加密算法
     * @return 返回加密后的字符串
     * @throws EncryptException 当加密发生错误时抛出该异常
     */
    public static String encrypt(String message, Encrypter.ENCRYPTER_TYPE type) throws EncryptException {
        return EncrypterFactory.getInstance().getEncrypter(type).encrypt(message);
    }

    /**
     * 用指定的算法对给定的字符串进行解密，并返回解密后的字符串。
     * 
     * @param message 预加密的字符串
     * @param type 解密算法
     * @return 返回解密后的字符串
     * @throws EncryptException 当解密发生错误时抛出该异常
     */
    public static String decrypt(String encryptedMessage, Encrypter.ENCRYPTER_TYPE type) throws EncryptException {
        return EncrypterFactory.getInstance().getEncrypter(type).decrypt(encryptedMessage);
    }

    /**
     * 用指定的算法对给定的字符串进行加密，并返回使用UrlEncoder加密后的字符串。
     * 
     * @param message 预加密的字符串
     * @param type 加密算法
     * @return 返回加密后的字符串
     * @throws EncryptException 当加密发生错误时抛出该异常
     */
    @SuppressWarnings("deprecation")
    public static String encryptAndUrlEncode(String message, Encrypter.ENCRYPTER_TYPE type) throws EncryptException {
        return URLEncoder.encode(encrypt(message, type));
    }

    /**
     * 用指定的算法对给定的字符串进行解密，并返回解密后的字符串。<font color='red'>此方法慎用！！</font>
     * 
     * @param message 预加密的字符串
     * @param type 解密算法
     * @return 返回解密后的字符串
     * @throws EncryptException 当解密发生错误时抛出该异常
     */
    @SuppressWarnings("deprecation")
    public static String decryptAndUrlDecode(String encryptedMessage, Encrypter.ENCRYPTER_TYPE type)
            throws EncryptException {
        return decrypt(URLDecoder.decode(encryptedMessage), type);
    }
}
