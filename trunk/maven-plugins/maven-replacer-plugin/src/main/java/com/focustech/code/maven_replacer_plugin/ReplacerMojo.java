/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_replacer_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

import com.focustech.code.maven_replacer_plugin.utils.MapUtils;
import com.focustech.code.maven_replacer_plugin.utils.ReplaceUtils;

/**
 * replace the text!!
 * 
 * @author taofucheng
 * @goal replace
 * @phase process-resources
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
@SuppressWarnings("rawtypes")
public class ReplacerMojo extends AbstractMojo {
    private static final String[] EMPTY_STRING_ARRAY = {};
    /**
     * if skip
     * 
     * @parameter default-value=false
     */
    private Boolean skipReplace = false;
    /**
     * read or write file with encoding
     * 
     * @parameter default-value="${project.build.sourceEncoding}"
     */
    private String encoding;
    /**
     * if ignore error when file is missed
     * 
     * @parameter default-value=true
     */
    private Boolean ignoreMissFile = true;
    /**
     * expect Map.
     * 
     * @parameter
     */
    private LinkedHashMap<String, String> expectMap;
    /**
     * expect sigle file
     * 
     * @parameter
     */
    private File expectSigleFile;
    /**
     * target sigle file.under ${basedir}/src/main.
     * 
     * @parameter
     */
    private String targetSigleFile;
    /**
     * split with "|".under webapp.
     * 
     * @parameter
     */
    private String targetMultiFile;
    /**
     * @parameter expression="${project.build.sourceDirectory}"
     */
    private File sourceDirectory;
    /**
     * The output directory into which to copy the resources.
     * 
     * @parameter expression="${project.build.outputDirectory}"
     */
    private File outputDirectory;
    /**
     * The list of resources we want to transfer.
     * 
     * @parameter expression="${project.resources}"
     */
    private List<Resource> resources;
    /**
     * Single directory for extra files to include in the WAR.
     * 
     * @parameter expression="${basedir}/src/main/webapp"
     */
    private File warSourceDirectory;

    /**
     * The directory where the webapp is built.
     * 
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     */
    private File webappDirectory;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipReplace != null && skipReplace) {
            getLog().info("skip replacer...");
            return;
        }
        getLog().info("replacer start replace...");
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        // 替换文本内容
        // 获取期望Map
        if (expectMap == null) {
            expectMap = new LinkedHashMap<String, String>();
        }
        if (expectSigleFile != null && expectSigleFile.isFile()) {
            expectMap.putAll(MapUtils.readMapFromFile(expectSigleFile, encoding));

        }
        else {
            if (ignoreMissFile != null && !ignoreMissFile) {
                throw new IllegalArgumentException("expectSigleFile[" + expectSigleFile
                        + "] can not be null or not exists!");
            }
            else {
                getLog().info("replacer ignore missing file...");
            }
        }
        List<String> includes = null;
        if (StringUtils.isNotBlank(targetSigleFile)) {
            targetSigleFile = StringUtils.replace(targetSigleFile, "\\", "/");
            targetSigleFile = targetSigleFile.startsWith("/") ? targetSigleFile.substring(1) : targetSigleFile;
            includes = new ArrayList<String>();
            includes.add(targetSigleFile);
        }
        else if (StringUtils.isNotBlank(targetMultiFile)) {
            targetMultiFile = StringUtils.replace(targetMultiFile, "\\", "/");
            targetMultiFile = targetMultiFile.startsWith("/") ? targetMultiFile.substring(1) : targetMultiFile;
            includes = Arrays.asList(targetMultiFile.split("\\|"));
        }
        else {
            if (ignoreMissFile != null && !ignoreMissFile) {
                throw new IllegalArgumentException("target file(s) all don't not exists!!");
            }
            else {
                getLog().info("target file(s) all don't not exists!!");
            }
        }
        boolean matched = true;
        boolean tmp = false;
        // 处理拷贝文件
        try {
            matched = processDir(sourceDirectory, outputDirectory, includes, null, true);
            for (Resource resource : resources) {
                File destRoot = outputDirectory;
                if (resource.getTargetPath() != null) {
                    destRoot = new File(outputDirectory, resource.getTargetPath());
                }
                tmp =
                        processDir(new File(resource.getDirectory()), destRoot, resource.getIncludes(),
                                resource.getExcludes(), true);
                if (!matched) {
                    matched = tmp;
                }
            }
            tmp = processDir(warSourceDirectory, webappDirectory, includes, null, false);
            if (!matched) {
                matched = tmp;
            }
        }
        catch (Exception exc) {
            throw new MojoExecutionException("wrap: " + exc.getMessage(), exc);
        }
        if (!matched && ignoreMissFile != null && !ignoreMissFile) {
            throw new IllegalArgumentException("target file(s) all don't not exists!!");
        }
    }

    protected boolean processDir(File srcRoot, File destRoot, List<String> srcIncludes, List<String> srcExcludes,
            boolean destAsSource) throws Exception {
        if ((srcRoot == null) || (!srcRoot.exists())) {
            return false;
        }
        if (destRoot == null) {
            throw new MojoFailureException("destination directory for " + srcRoot + " is null");
        }
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(srcRoot);
        if ((srcIncludes != null) && !srcIncludes.isEmpty()) {
            scanner.setIncludes(srcIncludes.toArray(EMPTY_STRING_ARRAY));
        }
        if ((srcExcludes != null) && !srcExcludes.isEmpty()) {
            scanner.setExcludes(srcExcludes.toArray(EMPTY_STRING_ARRAY));
        }
        scanner.addDefaultExcludes();
        scanner.scan();
        boolean matched = false;
        for (String name : scanner.getIncludedFiles()) {
            String tmp = StringUtils.replace(name, "\\", "/");
            tmp = tmp.startsWith("/") ? tmp.substring(1) : tmp;
            if (isContainedByTarget(tmp)) {
                SourceFile src = new SourceFile(srcRoot, destRoot, name, destAsSource);
                ReplaceUtils.replaceAndSave(expectMap, MapUtils.readMapFromFile(src.toFile(), encoding),
                        src.toDestFile(), encoding);
                matched = true;
            }
        }
        return matched;
    }

    /**
     * tmp is cotained in target file(s).
     * 
     * @param tmp
     * @return
     */
    private boolean isContainedByTarget(String tmp) {
        // targetSigleFile和tmp都不是以“/”开头的！
        if (targetSigleFile != null && targetSigleFile.equals(tmp)) {
            return true;
        }
        if (targetMultiFile != null) {
            for (String target : targetMultiFile.split("\\|")) {
                if (target.equals(tmp)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean getSkipReplace() {
        return skipReplace;
    }

    public void setSkipReplace(Boolean skipReplace) {
        this.skipReplace = skipReplace;
    }

    public String getTargetSigleFile() {
        return targetSigleFile;
    }

    public void setTargetSigleFile(String targetSigleFile) {
        this.targetSigleFile = targetSigleFile;
    }

    public String getTargetMultiFile() {
        return targetMultiFile;
    }

    public void setTargetMultiFile(String targetMultiFile) {
        this.targetMultiFile = targetMultiFile;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setExpectMap(LinkedHashMap<String, String> expectMap) {
        this.expectMap = expectMap;
    }

    public Map getExpectMap() {
        return expectMap;
    }

    public void setExpectSigleFile(File expectSigleFile) {
        this.expectSigleFile = expectSigleFile;
    }

    public File getExpectSigleFile() {
        return expectSigleFile;
    }

    public void setIgnoreMissFile(Boolean ignoreMissFile) {
        this.ignoreMissFile = ignoreMissFile;
    }

    public Boolean getIgnoreMissFile() {
        return ignoreMissFile;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public File getWarSourceDirectory() {
        return warSourceDirectory;
    }

    public void setWarSourceDirectory(File warSourceDirectory) {
        this.warSourceDirectory = warSourceDirectory;
    }

    public File getWebappDirectory() {
        return webappDirectory;
    }

    public void setWebappDirectory(File webappDirectory) {
        this.webappDirectory = webappDirectory;
    }
}
