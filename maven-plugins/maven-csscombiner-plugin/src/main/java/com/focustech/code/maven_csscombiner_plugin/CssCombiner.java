/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_csscombiner_plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 合并CSS的类（将@import换为相应的文件内容）
 * 
 * @author geliang
 */
public class CssCombiner {
    private static String cssDirPath = null;
    private static String _encoding;

    public CssCombiner(File cssDir, String encoding) {
        cssDirPath = cssDir.getAbsolutePath();
        _encoding = encoding;
    }

    /**
     * 入口的主方法
     */
    public void doCombine() {
        List<CssFile> cssFiles = new ArrayList<CssFile>();
        File dir = new File(cssDirPath);
        collectCssFiles(dir, cssFiles);
        buildRelation(cssFiles);
        combine(cssFiles);
    }

    /**
     * 收集指定目录及其子目录下所有的css文件
     * 
     * @param dir 指定的目录
     * @param cssFiles css文件收集器
     */
    private void collectCssFiles(File dir, List<CssFile> cssFiles) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".css")) {
                CssFile cssFile = new CssFile(f.getAbsolutePath());
                cssFiles.add(cssFile);
            }
            else if (f.isDirectory()) {
                collectCssFiles(f, cssFiles);
            }
        }
    }

    /**
     * 合并操作
     */
    public void combine(List<CssFile> files) {
        while (!isAllCombined(files)) {
            // 从底层开始向上合并
            for (CssFile f : files) {
                if (f.hasAppend()) {
                    continue;
                }
                f.doAppend();
            }
        }

        for (CssFile f : files) {
            f.reWrite();
        }
    }

    /**
     * 建立file之间的append关系
     * 
     * @param files
     * @throws IOException
     */
    public void buildRelation(List<CssFile> files) {
        Map<String, CssFile> cssFileHash = new HashMap<String, CssFile>();
        for (CssFile f : files) {
            cssFileHash.put(f.getFileName(), f);
        }
        for (CssFile f : files) {
            List<String> content = f.getContent();
            for (String line : content) {
                if (line.indexOf("@import") > -1) {
                    Pattern pattern = Pattern.compile(" url\\(\"(.+)\"\\)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        f.addAppend(cssFileHash.get(getRealFilePath(f, matcher.group(1))));
                    }
                }
            }
        }
    }

    private static String getRealFilePath(CssFile f, String importcssName) {
        String filePath = cssDirPath;
        if (importcssName.startsWith("/")) {
            filePath = filePath + importcssName;
        }
        else {
            filePath = f.getFileName().substring(0, f.getFileName().lastIndexOf(File.separator) + 1) + importcssName;
        }
        filePath = StringUtils.replace(filePath, "/", File.separator);
        return filePath;
    }

    /**
     * 是否所有的文件都合并完成
     * 
     * @param files 文件集合
     * @return
     */
    private boolean isAllCombined(List<CssFile> files) {
        for (CssFile f : files) {
            if (f.hasAppend()) {
                return false;
            }
        }
        return true;
    }

    /**
     * CSS文件辅助类
     */
    private static class CssFile {

        private List<CssFile> append = new ArrayList<CssFile>(); // 指代需要合并的子文件
        private List<CssFile> appendTo = new ArrayList<CssFile>(); // 指代合并此文件的父文件
        private String fileName;// 文件的完整路径和名称
        private List<String> content = new ArrayList<String>();
        private boolean isChanged = false;

        public CssFile(String fileName) {
            this.fileName = fileName;
            try {
                content = FileUtils.readLines(new File(fileName), _encoding);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 是否有尚未合并的子文件
         */
        public boolean hasAppend() {
            return append.size() > 0;
        }

        /**
         * 将自己合并到所有的父文件
         */
        public void doAppend() {
            for (CssFile toAppendFile : appendTo) {
                toAppendFile.combineIn(this);
            }
        }

        /**
         * 将传入的文件合并到当前文件中（将@import语句替换为相应的文件内容）
         * 
         * @param f 需要合并的子文件
         */
        public void combineIn(CssFile f) {
            for (int i = 0; i < this.content.size(); i++) {
                String line = this.content.get(i);
                if (line.indexOf("@import") > -1) {
                    Pattern pattern = Pattern.compile("url\\(\"(.+)\"\\)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String appendFileName = matcher.group(1);
                        if (getRealFilePath(this, appendFileName).equals(f.getFileName())) {
                            this.content.remove(i);
                            this.content.addAll(i, f.getContent());
                            break;
                        }
                    }
                }
            }
            append.remove(f);
            isChanged = true;
        }

        /**
         * 根据新的文件内容重新写文件
         */
        public void reWrite() {
            if (!isChanged) {
                return;
            }
            try {
                FileUtils.forceDelete(new File(this.getFileName()));
                FileUtils.writeLines(new File(this.getFileName()), _encoding, this.content);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 增加一个需要合并到自身的子文件。即，增加需要包含的文件
         * 
         * @param f
         */
        public void addAppend(CssFile f) {
            if (f != null) {
                append.add(f);
                f.getAppendTo().add(this);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CssFile)) {
                return false;
            }
            CssFile other = (CssFile) obj;
            if (other.getFileName().equals(this.fileName)) {
                return true;
            }
            else {
                return false;
            }

        }

        public List<CssFile> getAppendTo() {
            return this.appendTo;
        }

        public String getFileName() {
            return this.fileName;
        }

        public List<String> getContent() {
            return this.content;
        }

        public String toString() {
            return this.getFileName();
        }

    }
}
