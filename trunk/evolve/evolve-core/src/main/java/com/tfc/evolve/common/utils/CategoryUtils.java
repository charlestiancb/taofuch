/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目录工具类
 * 
 * @author taofucheng
 */
public class CategoryUtils {
    private static Map<String, List<Long>> industryCatCode = new HashMap<String, List<Long>>();

    /**
     * 获取指定的目录码信息的完整内容。即，如果长度不够，则在后面补足0. 如果提供的字符串超过或达到10位，则返回原字符串，即不做处理。
     * 
     * @param cateCode 目录码
     * @return 返回对应的目录码，或""。
     */
    public static String getWholeCateCode(String cateCode) {
        if (StringUtils.isEmpty(cateCode)) {
            return StringUtils.trimToEmpty(cateCode);
        }
        if (cateCode.length() > 9) {
            return cateCode;
        }
        else {
            return StringUtils.fillString(cateCode, "0", 10 - cateCode.length());
        }
    }

    /**
     * 获取指定目录码的前面非0部分。如果传入的值为空或空格之类，则返回null。
     * 
     * @param cateCode 指定的目录码
     * @return 返回指定目录码的前面非0部分。
     */
    public static String getPreNumber(String cateCode) {
        if (StringUtils.isBlank(cateCode)) {
            return null;
        }
        String tmp = StringUtils.reverse(cateCode);
        Long l = Long.parseLong(tmp);
        return StringUtils.fillString(StringUtils.reverse(l.toString()), "0", getLevelByCode(cateCode) * 2
                - l.toString().length());
    }

    /**
     * 获取指定目录码的前面非0部分。如果传入的值为空或空格之类，则返回null。
     * 
     * @param cateCode 指定的目录码
     * @return 返回指定目录码的前面非0部分。
     */
    public static String[] getPreNumber(String... cateCode) {
        if (cateCode == null || cateCode.length == 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (String cat : cateCode) {
            String tmp = getPreNumber(cat);
            if (tmp != null) {
                result.add(tmp);
            }
        }
        return result.toArray(new String[]{});
    }

    /**
     * 根据指定的目录计算出其结束的目录码。
     * 
     * @param catCode 目录码
     * @return
     */
    public static String getEndCatCode(Long cateCode) {
        if (cateCode == null || cateCode.longValue() < 1000000000L) {
            return "";
        }
        String tmp = String.valueOf(cateCode);
        tmp = StringUtils.reverse(tmp);
        tmp = StringUtils.reverse(String.valueOf(Long.parseLong(tmp)));// 这是去掉整个目录码后面所有的0之后的内容
        if (tmp.length() % 2 == 1) {
            // 如果长度不是偶数，则增加一位凑成偶数，因为目录级别是按偶数区分的
            tmp += "0";
        }
        cateCode = Long.parseLong(tmp) + 1;
        tmp = getWholeCateCode(String.valueOf(cateCode));
        return String.valueOf(Long.valueOf(tmp) - 1);
    }

    /**
     * 返回指定目录码所在的目录级别。如果指定的内容为空或不合法，则返回0
     * 
     * @param catCode 目录码。
     * @return
     */
    public static int getLevelByCode(String catCode) {
        catCode = StringUtils.trim(catCode);
        if (!isValidCode(catCode)) {
            return 0;
        }
        else {
            String tmp = StringUtils.reverse(catCode);
            tmp = String.valueOf(Long.parseLong(tmp));
            int level = tmp.length() / 2;
            if (tmp.length() % 2 == 1) {
                ++level;
            }
            return level;
        }
    }

    /**
     * 判断给定的字符串是否是合法的目录码
     * 
     * @param catCode
     * @return
     */
    public static boolean isValidCode(String catCode) {
        if (StringUtils.isBlank(catCode)) {
            return false;
        }
        if (!StringUtils.isInteger(catCode)) {
            return false;
        }
        long code = -1;
        try {
            code = Long.parseLong(catCode);
        }
        catch (Exception e) {
            return false;
        }
        if (code < 1000000000L || code > 9999999999L) {
            return false;
        }
        return true;
    }

    /**
     * 根据行业市场名称获取对应的所有一级目录码。如果传入的参数为空，或没有对应的行业目录信息，则返回null
     * 
     * @param industryName 行业市场名称，如：工业零部件
     * @return 返回行业市场名称对应的所有一级目录码
     */
    public static List<Long> getCatCodeByIndustry(String industryName) {
        industryName = StringUtils.trim(industryName);
        if (StringUtils.isEmpty(industryName)) {
            return null;
        }
        return industryCatCode == null ? null : industryCatCode.get(industryName);
    }

    public void setIndustryCatCode(Map<String, List<Long>> industryCatCode) {
        CategoryUtils.industryCatCode = industryCatCode;
    }

    /**
     * 获取所有行业市场名称对应的所有一级目录码
     * 
     * @return
     */
    public Map<String, List<Long>> getIndustryCatCode() {
        return industryCatCode;
    }
}
