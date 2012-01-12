package com.tfc.code.plugins.generation.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tfc.code.plugins.ClassUtil;

public class JavaTypeFactory {
    private static boolean hasInit = false;
    public static Map<String, JavaType> types = new HashMap<String, JavaType>();

    public static JavaType lookupByType(String type) {
        if (!hasInit) {
            // 扫描默认的定义！
            Set<Class<?>> classes = ClassUtil.getClasses(JavaType.class.getPackage());
            for (Class<?> cls : classes) {
                if (JavaType.class.isAssignableFrom(cls) && !cls.isInterface()) {
                    try {
                        registerType((JavaType) cls.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (type == null) {
            return new ObjectJavaType();
        }
        JavaType result = types.get(type.toLowerCase());
        if (result == null) {
            return new ObjectJavaType();
        } else {
            return result;
        }
    }

    public static void registerType(JavaType javaType) {
        if (javaType != null && javaType.getName() != null) {
            for (String name : javaType.getName()) {
                types.put(name, javaType);
            }
        }
    }
}
