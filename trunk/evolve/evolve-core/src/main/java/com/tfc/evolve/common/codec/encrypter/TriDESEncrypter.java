/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.tfc.evolve.common.codec.EncryptException;

/**
 * 三重DES,针对String加密/解密
 * 
 * @author taofucheng
 */
public class TriDESEncrypter implements Encrypter {
    private static final Log logger = LogFactory.getLog(TriDESEncrypter.class);
    private final StandardPBEByteEncryptor byteEncryptor;
    private final StandardPBEStringEncryptor stringEncryptor;

    public TriDESEncrypter() {
        byteEncryptor = new StandardPBEByteEncryptor();
        byteEncryptor.setAlgorithm(Encrypter.ENCRYPTER_TYPE.PBEWithMD5AndTripleDES.name());
        byteEncryptor.setPassword(Encrypter.PASSWORD.ABIZ_KEY.name());
        //
        stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setAlgorithm(Encrypter.ENCRYPTER_TYPE.PBEWithMD5AndTripleDES.name());
        stringEncryptor.setPassword(Encrypter.PASSWORD.ABIZ_KEY.name());
    }

    @Override
    public byte[] decrypt(byte[] encryptedMessage) throws EncryptException {
        try {
            return byteEncryptor.decrypt(encryptedMessage);
        }
        catch (Exception ex) {
            logger.error("解密[" + encryptedMessage + "]异常", ex);
            throw new EncryptException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }

    }

    @Override
    public byte[] encrypt(byte[] message) throws EncryptException {
        try {
            return byteEncryptor.encrypt(message);
        }
        catch (Exception ex) {
            logger.error("加密[" + message + "]异常", ex);
            throw new EncryptException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }

    }

    @Override
    public String encrypt(String message) throws EncryptException {
        try {
            return stringEncryptor.encrypt(message);
        }
        catch (Exception ex) {
            logger.error("加密[" + message + "]异常", ex);
            throw new EncryptException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }

    }

    @Override
    public String decrypt(String encryptedMessage) throws EncryptException {
        try {
            return stringEncryptor.decrypt(encryptedMessage);
        }
        catch (Exception ex) {
            logger.error("解密[" + encryptedMessage + "]异常", ex);
            throw new EncryptException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }

    }

}
