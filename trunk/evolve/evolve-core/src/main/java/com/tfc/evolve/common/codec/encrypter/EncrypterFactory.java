/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

import com.tfc.evolve.common.codec.encrypter.Encrypter.ENCRYPTER_TYPE;

/**
 * 单例模式& 工厂模式
 * 
 * @author taofucheng
 */
public class EncrypterFactory {
    private static EncrypterFactory factory = new EncrypterFactory();

    public static EncrypterFactory getInstance() {
        return factory;
    }

    private EncrypterFactory() {

    }

    public Encrypter getEncrypter(ENCRYPTER_TYPE algorithm) {
        switch (algorithm) {
            case PBEWithMD5AndDES :
                return new DESEncrypter();
            case PBEWithMD5AndTripleDES :
                return new TriDESEncrypter();
        }
        return null;
    }
}
