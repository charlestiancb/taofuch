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
 * SHA256算法,256位摘要
 * 
 * @author taofucheng
 */
public class SHA256Digester implements Digester {

    private static final Log logger = LogFactory.getLog(SHA256Digester.class);

    @Override
    public byte[] digest(byte[] message) throws DigestedException {
        try {
            return DigestUtils.sha256(message);
        }
        catch (Exception ex) {
            logger.error("摘要异常", ex);
            throw new DigestedException(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    @Override
    public String digest(String message) throws DigestedException {
        try {
            return DigestUtils.sha256Hex(message);
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
            throw new DigestedException(ex);
        }
    }
}
