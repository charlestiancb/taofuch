/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.tfc.evolve.common.codec.EncryptException;

/**
 * DES,针对String加密/解密
 * 
 * @author taofucheng
 */
public class DESEncrypter implements Encrypter {
    private final StandardPBEByteEncryptor byteEncryptor;
    private final StandardPBEStringEncryptor stringEncryptor;
    private static final Log logger = LogFactory.getLog(DESEncrypter.class);

    public DESEncrypter() {
        byteEncryptor = new StandardPBEByteEncryptor();
        byteEncryptor.setAlgorithm(Encrypter.ENCRYPTER_TYPE.PBEWithMD5AndDES.name());
        byteEncryptor.setPassword(Encrypter.PASSWORD.ABIZ_KEY.name());
        //
        stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setAlgorithm(Encrypter.ENCRYPTER_TYPE.PBEWithMD5AndDES.name());
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
