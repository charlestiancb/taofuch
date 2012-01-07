/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.digester;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tfc.evolve.common.codec.DigestedException;

/**
 * MD5算法,一般强度摘要
 * 
 * @author taofucheng
 */
public class MD5Digester implements Digester {

    private static final Log logger = LogFactory.getLog(MD5Digester.class);

    @Override
    public byte[] digest(byte[] message) throws DigestedException {
        try {
            return DigestUtils.md5(message);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    @Override
    public String digest(String message) throws DigestedException {
        try {
            return DigestUtils.md5Hex(message);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    @Override
    public boolean matches(byte[] message, byte[] digest) throws DigestedException {
        try {
            return Arrays.equals(digest(message), digest);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    @Override
    public boolean matches(String message, String digest) throws DigestedException {
        try {
            return digest(message).equals(digest);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }
}
