/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.digester;

import com.tfc.evolve.common.codec.DigestedException;

/**
 * 摘要服务工具类！
 * 
 * @author taofucheng
 */
public class DigesterUtils {
    /**
     * 获取给定的字节码数组的摘要信息。同一字节数组每次加密后的结果是不相同的！如果需要判断加密值与原值是否一致，请使用matchs方法！
     * 
     * @param message 字节码信息
     * @param type 指定的摘要算法。
     * @return 返回对应的摘要信息。
     * @throws DigestedException 处理异常时抛出该异常
     */
    public static byte[] digest(byte[] message, Digester.DIGESTER_TYPE type) throws DigestedException {
        return DigesterFactory.getInstance().getDigester(type).digest(message);
    }

    /**
     * 判断message的摘要与digest是否相同。
     * 
     * @param message 字节码信息
     * @param digest 摘要信息
     * @param type 指定的摘要算法。
     * @return 返回对应的摘要信息。
     * @throws DigestedException 处理异常时抛出该异常
     */
    public static boolean matches(byte[] message, byte[] digest, Digester.DIGESTER_TYPE type) throws DigestedException {
        return DigesterFactory.getInstance().getDigester(type).matches(message, digest);
    }

    /**
     * 获取给定的字符串的摘要信息。同一字符串每次加密后的结果是不相同的！如果需要判断加密值与原值是否一致，请使用matchs方法！
     * 
     * @param message 字节码信息
     * @param type 指定的摘要算法。
     * @return 返回对应的摘要信息。
     * @throws DigestedException 处理异常时抛出该异常
     */
    public static String digest(String message, Digester.DIGESTER_TYPE type) throws DigestedException {
        return DigesterFactory.getInstance().getDigester(type).digest(message);
    }

    /**
     * 判断message的摘要与digest是否相同。
     * 
     * @param message 字节码信息
     * @param digest 摘要信息
     * @param type 指定的摘要算法。
     * @return 返回对应的摘要信息。
     * @throws DigestedException 处理异常时抛出该异常
     */
    public static boolean matches(String message, String digest, Digester.DIGESTER_TYPE type) throws DigestedException {
        return DigesterFactory.getInstance().getDigester(type).matches(message, digest);
    }
}
