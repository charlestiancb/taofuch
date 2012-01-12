/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.code.maven_copyfile_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * CopyFileMojo.java
 * 
 * @author taofucheng
 * @goal copy
 * @phase process-resources
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class CopyFileMojo extends AbstractMojo {
    private static final String[] EMPTY_STRING_ARRAY = {};
    /**
     * read or write file with encoding
     * 
     * @parameter default-value="${project.build.sourceEncoding}"
     */
    private String encoding;
    /**
     * igonre If exists!
     * 
     * @parameter default-value=true
     */
    private boolean igonreIfExists;
    /**
     * list of additionnal excludes.use like as:<br>
     * 
     * <pre>
     * &lt;excludes&gt;
     *   &lt;exclude&gt;filePatten.ext&lt;/exclude&gt;
     * &lt;excludes&gt;
     * </pre>
     * 
     * @parameter
     */
    private List<String> excludes;
    /**
     * list of additionnal excludes.use like as:<br>
     * 
     * <pre>
     * &lt;includes&gt;
     *   &lt;include&gt;filePatten.ext&lt;/include&gt;
     * &lt;includes&gt;
     * </pre>
     * 
     * @parameter
     */
    private List<String> includes;
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
        getLog().info("start copyFile...");
        if (includes == null || includes.isEmpty()) {
            includes = new ArrayList<String>();
            includes.add("**");
        }
        try {
            processDir(sourceDirectory, sourceDirectory, includes, excludes);
            processDir(warSourceDirectory, webappDirectory, includes, excludes);
        }
        catch (Exception e) {
            throw new MojoFailureException("[maven-copyfile-plugin] copy file failure!", e);
        }
    }

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
            scanner.setIncludes(srcIncludes.toArray(EMPTY_STRING_ARRAY));
        }
        if ((srcExcludes != null) && !srcExcludes.isEmpty()) {
            scanner.setExcludes(srcExcludes.toArray(EMPTY_STRING_ARRAY));
        }
        scanner.addDefaultExcludes();
        scanner.scan();
        for (String name : scanner.getIncludedFiles()) {
            File srcFile = new File(srcRoot, name);
            File destFile = new File(destRoot, name);
            if (igonreIfExists && destFile.isFile()) {
                getLog().info("skip copy: " + destFile);
                continue;
            }
            FileUtils.copyFile(srcFile, destFile);
            getLog().info("copied succ: " + destFile);
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public void setIgonreIfExists(boolean igonreIfExists) {
        this.igonreIfExists = igonreIfExists;
    }

    public boolean isIgonreIfExists() {
        return igonreIfExists;
    }
}
