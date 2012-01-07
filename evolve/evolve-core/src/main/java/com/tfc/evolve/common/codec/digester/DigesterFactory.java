/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.digester;

/**
 * 单例模式& 工厂模式
 * 
 * @author taofucheng
 */
public class DigesterFactory {

    private static DigesterFactory factory = new DigesterFactory();

    private DigesterFactory() {

    }

    public static DigesterFactory getInstance() {
        return factory;
    }

    public Digester getDigester(Digester.DIGESTER_TYPE algorithm) {
        switch (algorithm) {
            case MD5 :
                return new MD5Digester();
            case SHA256 :
                return new SHA256Digester();
        }
        return null;
    }
}
