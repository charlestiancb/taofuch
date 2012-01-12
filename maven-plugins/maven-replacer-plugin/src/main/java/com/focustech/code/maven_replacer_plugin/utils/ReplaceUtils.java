/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_replacer_plugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ReplaceUtils.java
 * 
 * @author taofucheng
 */
public class ReplaceUtils {
    /**
     * 将预期的键值变换到目标的键值中，并将文件进行保存！
     * 
     * @param expectMap
     * @param targetMap
     * @param targetFile
     */
    public static void replaceAndSave(LinkedHashMap<String, String> expectMap, LinkedHashMap<String, String> targetMap,
            File targetFile, String encode) {
        if (expectMap == null || expectMap.isEmpty() || targetMap == null || targetMap.isEmpty()) {
            return;
        }
        if (targetFile == null) {
            throw new IllegalArgumentException("target file can not be null!!");
        }
        // 替换操作
        for (String key : expectMap.keySet()) {
            String value = targetMap.get(key);
            if (value != null) {
                // 存在这样的值，则直接put进去
                targetMap.put(key, expectMap.get(key));
            }
        }
        // 保存文件
        String fileContent = "";
        String lineSeparator = System.getProperty("line.separator");
        for (String key : targetMap.keySet()) {
            fileContent +=
                    (key.startsWith("#") || key.startsWith(" ") ? StringUtils.trim(key.substring(0, 1)) : (key + "="))
                            + targetMap.get(key) + (StringUtils.isBlank(lineSeparator) ? "\r\n" : lineSeparator);
        }
        try {
            FileUtils.writeStringToFile(targetFile, fileContent, encode);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
