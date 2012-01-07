/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

import com.tfc.evolve.common.codec.EncryptException;

/**
 * 加密/解密器，采用固定的基于口令的密码
 * 
 * @author taofucheng
 */
public interface Encrypter {

    public enum PASSWORD {
        ABIZ_KEY
    }

    public enum ENCRYPTER_TYPE {
        PBEWithMD5AndDES, PBEWithMD5AndTripleDES
    }

    /**
     * 对指定的字节流进行加密，并以字节流返回
     * 
     * @param message 指定的字节流
     * @return 返回加密后的字节流
     * @throws EncryptException 如果加密失败，则抛出该异常
     */
    public byte[] encrypt(byte[] message) throws EncryptException;

    /**
     * 对指定的字节流进行解密，并以字节流返回
     * 
     * @param message 指定的字节流
     * @return 返回解密后的字节流
     * @throws EncryptException 如果解密失败，则抛出该异常
     */
    public byte[] decrypt(byte[] encryptedMessage) throws EncryptException;

    /**
     * 对指定的字符串进行加密，并以字符串返回
     * 
     * @param message 指定的字符串
     * @return 返回加密后的字符串
     * @throws EncryptException 如果加密失败，则抛出该异常
     */
    public String encrypt(String message) throws EncryptException;

    /**
     * 对指定的字符串进行解密，并以字符串返回
     * 
     * @param message 指定的字符串
     * @return 返回解密后的字符串
     * @throws EncryptException 如果解密失败，则抛出该异常
     */
    public String decrypt(String encryptedMessage) throws EncryptException;
}
