/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_replacer_plugin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * PropertyUtils.java
 * 
 * @author taofucheng
 */
@SuppressWarnings("rawtypes")
public class MapUtils {
    /**
     * 将文件读入为资源文件方式
     * 
     * @param file
     * @return
     */
    public static Properties readFromFile(File file) {
        Properties pro = new Properties();
        if (file.exists()) {
            InputStream ins = null;
            try {
                ins = new FileInputStream(file);
                pro.load(ins);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        return pro;
    }

    public static Properties readFromFileName(String fileFullPath) {
        if (StringUtils.isBlank(fileFullPath)) {
            return new Properties();
        }
        else {
            return readFromFile(new File(fileFullPath));
        }
    }

    public static LinkedHashMap readFromProperties(Properties pro) {
        LinkedHashMap m = new LinkedHashMap();
        if (pro != null) {
        }
        return m;
    }

    public static LinkedHashMap<String, String> readMapFromFile(File file, String encoding) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        try {
            List<String> lines = FileUtils.readLines(file, StringUtils.defaultIfBlank(encoding, null));
            if (lines != null && lines.size() > 0) {
                int _idx = 0;
                for (String line : lines) {
                    if (StringUtils.isBlank(line)) {
                        result.put(" " + (_idx++), StringUtils.isEmpty(line) ? "" : line);
                        continue;
                    }
                    if (line.startsWith("#")) {
                        result.put("#" + (_idx++), line.substring(1));
                        continue;
                    }
                    // 将第一个=等号前面的内容作为key，将后面的内容作为value，如果没有等号，或等号后面的内容为空，则直接value=""
                    int idx = line.indexOf("=");
                    String key = "";
                    String value = "";
                    if (idx == -1 || line.length() == idx + 1) {
                        key = line.endsWith("=") ? line.substring(0, idx) : line;
                    }
                    else {
                        key = line.substring(0, idx);
                        value = line.substring(idx + 1);
                    }
                    result.put(key, value);
                }
            }
        }
        catch (IOException e) {
        }
        return result;
    }

    public static LinkedHashMap<String, String> readMapFromFileName(String fileFullPath, String encoding) {
        if (StringUtils.isBlank(fileFullPath)) {
            return new LinkedHashMap<String, String>();
        }
        else {
            return readMapFromFile(new File(fileFullPath), encoding);
        }
    }
}
