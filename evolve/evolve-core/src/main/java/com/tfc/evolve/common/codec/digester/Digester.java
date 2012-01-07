/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.digester;

import com.tfc.evolve.common.codec.DigestedException;

/**
 * 摘要通用接口
 * 
 * @author taofucheng
 */
public interface Digester {
    public enum DIGESTER_TYPE {
        MD5, SHA256
    }

    public byte[] digest(byte[] message) throws DigestedException;

    public boolean matches(byte[] message, byte[] digest) throws DigestedException;

    public String digest(String message) throws DigestedException;

    public boolean matches(String message, String digest) throws DigestedException;

}
