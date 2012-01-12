/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_csscombiner_plugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Combine css from "import" keyword
 * 
 * @author taofucheng,geliang
 * @goal combine
 * @phase prepare-package
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class CssCombinerMojo extends AbstractMojo {
    /**
     * relative path in project. such as /style/. defult-value is "/"
     * 
     * @parameter default-value="/"
     */
    private String cssDirPath;
    /**
     * css file encoding. defult-value is "${project.build.sourceEncoding}"
     * 
     * @parameter default-value="${project.build.sourceEncoding}"
     */
    private String encoding;
    /**
     * exclude file without process.
     * 
     * @parameter
     */
    private String[] excludes;
    /**
     * include file without process.
     * 
     * @parameter
     */
    private String[] includes;
    /**
     * special if skip. if null or false will be skiped!
     * 
     * @parameter default-value=false
     */
    private Boolean skip;
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

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip != null && skip) {
            getLog().info("skip cssCombiner...");
            return;
        }
        getLog().info("start cssCombiner...");
        // 先复制文件过来！
        if (includes == null || includes.length == 0) {
            includes = new String[]{"**/*.css"};
        }
        try {
            processDir(warSourceDirectory, webappDirectory, Arrays.asList(includes), null);
        }
        catch (Exception e) {
            getLog().error("copy css files occour error!!", e);
        }
        // 组织并判断指定的文件是否存在。
        File cssDirPathFile = null;
        if (StringUtils.isBlank(getCssDirPath()) || StringUtils.trimToEmpty(getCssDirPath()).equals("/")) {
            cssDirPathFile = new File(webappDirectory.getAbsolutePath());
        }
        else {
            cssDirPathFile = new File(webappDirectory.getAbsolutePath(), getCssDirPath());
        }
        if (cssDirPathFile == null || !cssDirPathFile.isDirectory()) {
            throw new MojoFailureException("cssDirPath[" + cssDirPathFile + "] is null or is not directory!");
        }
        // 处理import依赖
        new CssCombiner(cssDirPathFile, getEncoding()).doCombine();
    }

    /**
     * 将所有的css文件复制过来。
     * 
     * @param srcRoot
     * @param destRoot
     * @param srcIncludes
     * @param srcExcludes
     * @throws Exception
     */
    protected void processDir(File srcRoot, File destRoot, List<String> srcIncludes, List<String> srcExcludes)
            throws Exception {
        if ((srcRoot == null) || (!srcRoot.exists())) {
            return;
        }
        if (destRoot == null) {
            throw new MojoFailureException("destination directory for " + srcRoot + " is null");
        }
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(srcRoot);
        if ((srcIncludes != null) && !srcIncludes.isEmpty()) {
            scanner.setIncludes(srcIncludes.toArray(new String[]{}));
        }
        if ((srcExcludes != null) && !srcExcludes.isEmpty()) {
            scanner.setExcludes(srcExcludes.toArray(new String[]{}));
        }
        scanner.addDefaultExcludes();
        scanner.scan();
        for (String name : scanner.getIncludedFiles()) {
            File destFile = new File(webappDirectory, name);
            if (destFile.isFile()) {
                FileUtils.forceDelete(destFile);
            }
            FileUtils.copyFile(new File(warSourceDirectory, name), destFile);
        }
    }

    public void setCssDirPath(String cssDirPath) {
        this.cssDirPath = cssDirPath;
    }

    public String getCssDirPath() {
        return cssDirPath;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public Boolean getSkip() {
        return skip;
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

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }
}
