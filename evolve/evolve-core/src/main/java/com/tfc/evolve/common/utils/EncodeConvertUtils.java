/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.io.UnsupportedEncodingException;

/**
 * 编码转换工具类
 * 
 * @author taofucheng
 */
public class EncodeConvertUtils {
    /**
     * 将指定的字符串从UTF转换为GBK编码
     * 
     * @param str 字符串
     * @return 返回GBK编码字符串。如果出现编码不支持的现象，则返回原字符串。
     */
    public static String utf8ToGbk(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        char c[] = str.toCharArray();
        String result = "";
        for (int i = 0; i < c.length; i++) {
            byte[] source = null;
            try {
                source = String.valueOf(c[i]).getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                return str;
            }
            if (source.length == 1) {
                result += c[i];
            }
            else if (source.length == 2) {
                // 两个byte的对应格式为：110xxxxx 10xxxxxx
                String s1 = Integer.toBinaryString(source[0]);
                String s2 = Integer.toBinaryString(source[1]);
                s1 = s1.substring(s1.length() - 8);
                s2 = s2.substring(s2.length() - 8);
                s1 = s1.substring(3);
                s2 = s2.substring(2);
                String tmp = s1 + s2;
                char[] cc = Character.toChars(Integer.valueOf(tmp, 2));
                result += cc[0];
            }
            else if (source.length == 3) {
                // 三个byte的对应格式为：1110xxxxx 10xxxxxx 10xxxxxx
                String s1 = Integer.toBinaryString(source[0]);
                String s2 = Integer.toBinaryString(source[1]);
                String s3 = Integer.toBinaryString(source[2]);
                s1 = s1.substring(s1.length() - 8);
                s2 = s2.substring(s2.length() - 8);
                s3 = s3.substring(s3.length() - 8);

                s1 = s1.substring(4);
                s2 = s2.substring(2);
                s3 = s3.substring(2);

                String tmp = s1 + s2 + s3;
                char[] cc = Character.toChars(Integer.valueOf(tmp, 2));
                result += cc[0];
            }
            else {
                result += c[i];
            }
        }
        return result;
    }
}
