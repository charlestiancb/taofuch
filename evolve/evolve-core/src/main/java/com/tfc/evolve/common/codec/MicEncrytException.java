/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec;

/**
 * Mic加密异常
 * 
 * @author taofucheng
 */
public class MicEncrytException extends RuntimeException {

    private static final long serialVersionUID = 7592524571847719831L;

    /**
     * 创建一个异常。
     */
    public MicEncrytException() {
        super();
    }

    /**
     * 创建一个异常。
     * 
     * @param message 异常信息
     */
    public MicEncrytException(String message) {
        super(message);
    }

    /**
     * 创建一个异常。
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public MicEncrytException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建一个异常。
     * 
     * @param cause 异常原因
     */
    public MicEncrytException(Throwable cause) {
        super(cause);
    }
}
