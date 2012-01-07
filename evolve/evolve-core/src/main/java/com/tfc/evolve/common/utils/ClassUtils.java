/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类操作的相关工具类
 * 
 * @author taofucheng
 */
public class ClassUtils extends org.apache.commons.lang.ClassUtils {
    /**
     * 判断指定的类是否是基本类型的数组。如果转入参数为空，则返回false
     * 
     * @param clazz 指定的类。
     * @return true:是;false：否
     */
    public static boolean isPrimitiveArray(Object value) {
        Class<?> type = getObjType(value);
        return int.class == type || long.class == type || char.class == type || byte.class == type
                || short.class == type || float.class == type || double.class == type || boolean.class == type;
    }

    /**
     * 判断指定的类是否是基本类型的整数型的数组。如果转入参数为空，则返回false
     * 
     * @param value
     * @return
     */
    public static boolean isIntPrimitiveArray(Object value) {
        Class<?> type = getObjType(value);
        return int.class == type || long.class == type || char.class == type || byte.class == type
                || short.class == type;
    }

    /**
     * 判断指定的类是否是基本类型的浮点型的数组。如果转入参数为空，则返回false
     * 
     * @param value
     * @return
     */
    public static boolean isFloatPrimitiveArray(Object value) {
        Class<?> type = getObjType(value);
        return float.class == type || double.class == type;
    }

    /**
     * 判断指定的类是否是基本类型的布尔型的数组。如果转入参数为空，则返回false
     * 
     * @param value
     * @return
     */
    public static boolean isBooleanPrimitiveArray(Object value) {
        Class<?> type = getObjType(value);
        return boolean.class == type;
    }

    private static Class<?> getObjType(Object value) {
        if (!value.getClass().isArray()) {
            return null;
        }
        return value.getClass().getComponentType();
    }

    /**
     * 在指定的类及其父类中查找指定属性，并返回该属性的Field。
     * 
     * @param clazz 指定的类(注意，不是对象)
     * @param fieldName 属性名称。
     * @return 返回属性对应的Field
     */
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        fieldName = StringUtils.trim(fieldName);
        if (StringUtils.isEmpty(fieldName) || clazz == null) {
            return null;
        }
        Field f = null;
        try {
            f = clazz.getDeclaredField(fieldName);
        }
        catch (Exception e) {
        }
        if (f != null) {
            return f;
        }
        else if (clazz.getSuperclass() != null) {
            return getFieldByName(clazz.getSuperclass(), fieldName);
        }
        else {
            return null;
        }
    }

    /**
     * 获取指定类的所有属性，包括其父类中的
     * 
     * @param clazz 指定的类
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Field> getAllFileds(Class<? extends Object> clazz) {
        List<Field> result = new ArrayList<Field>();
        List<Class<?>> clazzs = getAllSuperclasses(clazz);
        clazzs.add(clazz);
        for (Class<?> c : clazzs) {
            if (c.getDeclaredFields() != null && c.getDeclaredFields().length > 0) {
                for (Field f : c.getDeclaredFields()) {
                    result.add(f);
                }
            }
        }
        return result;
    }

    /**
     * 克隆对象。不能完全支持深度复制。如果复制失败了，则返回null.
     * 
     * @param obj
     * @return
     */
    public static Object clone(Object obj) {
        if (obj == null) {
            return null;
        }
        else if (obj.getClass().isPrimitive() || obj instanceof Number || obj instanceof String) {
            return obj;
        }
        else if (obj instanceof Cloneable) {
            Class<?> clazz = obj.getClass();
            Method m;
            try {
                m = clazz.getMethod("clone");
                return m.invoke(obj);
            }
            catch (Exception ex) {
                return null;
            }
        }
        else {
            try {
                Object o = obj.getClass().newInstance();
                ReflectUtils.copyPropertis(obj, o);
                return o;
            }
            catch (Exception e) {
                return obj;
            }
        }
    }
}
