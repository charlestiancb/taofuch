/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具
 * 
 * @author taofucheng
 */
public class FileUtils {
    // 设置临时文件目录产生规则
    private static String tempDir = "";
    static {
        if (tempDir == null || "".equals(tempDir)) {
            tempDir = System.getProperty("java.io.tmpdir");
        }
        tempDir += "/";

    }

    /**
     * 得到随机数(6位以内),防止出现同一时间生成文件时,只用毫秒区分文件导致的文件被覆盖
     */
    protected static String getRandomString() {
        return String.valueOf((int) (Math.random() * 1000000));
    }

    /**
     * 创建一个0字节的文件。
     * 
     * @author taofucheng
     * @param format 指定的文件后缀（其实可以是任意后缀）。如果没有，则返回没有后缀的文件。命名方式是当前时间与随机数组合。
     * @return
     * @throws Exception
     */
    public static String createTempFile(String format) throws Exception {
        String tempFilePath = tempDir + System.currentTimeMillis() + getRandomString() + getFileFormat(format);
        File file = new File(tempFilePath);
        file.createNewFile();
        return tempFilePath;

    }

    /**
     * 获取文件后缀名
     * 
     * @param format 指定的后缀名
     * @return 如果指定了后缀名，则在前面加个"."，否则返回"".
     */
    private static String getFileFormat(String format) {
        format = StringUtils.trimToEmpty(format);
        if (StringUtils.isNotEmpty(format)) {
            return "." + format;
        }
        else {
            return "";
        }
    }

    /**
     * 根据字节码生成临时文件。
     * 
     * @param data 文件具体内容
     * @param format 指定文件的格式
     * @return 返回生成的文件的完整路径名称
     * @throws Exception 如果提供的内容为空，或生成过程中失败，则抛出该异常
     */
    public static String createTempFile(byte data[], String format) throws Exception {
        if (data == null || data.length < 1) {
            throw new Exception("data is null or empty!");
        }
        OutputStream outputStream = null;
        String tempFilePath = tempDir + System.currentTimeMillis() + getRandomString() + getFileFormat(format);
        try {
            outputStream = new FileOutputStream(tempFilePath);
            outputStream.write(data);
            outputStream.flush();
            if (outputStream != null)
                outputStream.close();
        }
        catch (Exception e) {
            if (outputStream != null)
                outputStream.close();
            throw e;
        }
        return tempFilePath;
    }

    /**
     * 根据输入流建立临时文件。
     * 
     * @param inputStream 文件具体内容
     * @param format 指定文件的格式
     * @return 返回临时文件的位置。
     * @throws Exception 如果提供的内容为空，或生成过程中失败，则抛出该异常
     */
    protected static String createTempFile(InputStream inputStream, String format) throws Exception {
        if (inputStream == null || inputStream.available() < 1) {
            throw new Exception("inputStream is null or empty!");
        }
        OutputStream outputStream = null;
        String tempFilePath = tempDir + System.currentTimeMillis() + getRandomString() + getFileFormat(format);
        try {
            outputStream = new FileOutputStream(tempFilePath);
            byte[] buffer = new byte[2048];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, 2048)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            if (outputStream != null)
                outputStream.close();
        }
        catch (Exception e) {
            if (outputStream != null)
                outputStream.close();
            throw e;
        }
        return tempFilePath;
    }
}
