package com.tfc.word.utils;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

public class ClasspathUtils {
    /** Maven默认路径下的资源文件 */
    public static final String MAVEN_RESOURCE_PATH = "/main/resources";

    public static InputStream loadFromClassPath(String classpath) {
        if (StringUtils.isBlank(classpath)) {
            return null;
        }
        classpath = StringUtils.trim(classpath);
        classpath = classpath.startsWith("/") ? classpath : "/" + classpath;
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(MAVEN_RESOURCE_PATH + classpath);
        }
        if (is == null) {
            is = ClasspathUtils.class.getResourceAsStream(classpath);
        }
        if (is == null) {
            is = ClasspathUtils.class.getResourceAsStream(MAVEN_RESOURCE_PATH + classpath);
        }
        return is;
    }
}
