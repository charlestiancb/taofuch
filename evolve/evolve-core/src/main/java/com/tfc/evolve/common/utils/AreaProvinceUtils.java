/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 大区与省份对应关系的工具类。这个工具可以获取大区所辖的省份，也可以查询省份所属的大区。<br>
 * 注意：这里的大区、省份都是简称。
 * 
 * @author taofucheng
 */
public class AreaProvinceUtils {
    /** 省份与城市对应关系数据结构 */
    private static Map<String, Map<String, String>> areaProv = new LinkedHashMap<String, Map<String, String>>();

    /**
     * 根据省份获取其所属的大区。如果参数为空，则查不到对应的key值，则返回null。
     * 
     * @param province 省份key值，如Hainan
     * @return 返回对应的大区信息，或null。
     */
    public static String getAreaByProvince(String province) {
        if (StringUtils.isEmpty(province)) {
            return null;
        }
        if (!areaProv.isEmpty()) {
            for (String key : areaProv.keySet()) {
                Map<String, String> pros = areaProv.get(key);
                if (pros != null && pros.size() > 0
                        && StringUtils.isContained(province, pros.keySet().toArray(new String[pros.size()]))) {
                    return key;
                }
            }
        }
        return null;
    }

    /**
     * 获取大区对应的所有省份key值信息。如果参数为空，则查不到对应的key值，则返回null。
     * 
     * @param area 指定的大区key值
     * @return 返回对应的所有省份key值信息，或null。
     */
    public static String[] getProvinceKeysByArea(String area) {
        if (StringUtils.isEmpty(area)) {
            return null;
        }
        Map<String, String> pros = areaProv.get(area);
        return pros == null || pros.size() < 1 ? null : pros.keySet().toArray(new String[pros.size()]);
    }

    /**
     * 获取省份的完整信息。以键值对方式返回对应的key与value。
     * 
     * @param area 大区code信息，如：Huadong。
     * @return 返回完整的省份key值对，如：{Anhui:安徽}，或null
     */
    public static Map<String, String> getProvincesByArea(String area) {
        if (StringUtils.isEmpty(area)) {
            return null;
        }
        return areaProv.get(area);
    }

    /**
     * 获取所有大区对应的省份信息。这里的省份是简写，如安徽。
     * 
     * @return 返回完整的省份key值对，如：{Anhui:安徽}，或null
     */
    public static Map<String, String> getAllAreaProvinces() {
        if (areaProv != null && areaProv.size() > 0) {
            Map<String, String> result = new LinkedHashMap<String, String>();
            for (String key : areaProv.keySet()) {
                result.putAll(areaProv.get(key));
            }
            return result;
        }
        return null;
    }

    /**
     * 根据指定的省份key值或key值数组获取对应的所有省份信息。
     * 
     * @param key 对应的省份key值或key值数组
     * @return 返回完整的省份key值对，如：{Anhui:安徽}，或null
     */
    public static Map<String, String> getProvsByKey(String... key) {
        Map<String, String> provs = getAllAreaProvinces();
        if (key == null || key.length < 1 || provs == null || provs.size() < 1) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        for (String k : key) {
            if (StringUtils.isNotEmpty(k) && StringUtils.isNotEmpty(provs.get(k))) {
                result.put(k, provs.get(k));
            }
        }
        return result;
    }

    /**
     * 属性getter方法
     * 
     * @return
     */
    public Map<String, Map<String, String>> getAreaProv() {
        return areaProv;
    }

    /**
     * 属性setter方法
     */
    public void setAreaProv(Map<String, Map<String, String>> areaProv) {
        AreaProvinceUtils.areaProv = areaProv;
    }
}
