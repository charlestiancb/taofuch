/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class to handle reflection on java objects. The class contains static methods to call reflection methods,
 * catch any exceptions, converting them to BuildExceptions.
 * 
 * @author yangxin,taofucheng
 */
@SuppressWarnings("rawtypes")
public class ReflectUtils {

    /**
     * Call a method on the object with no parameters.
     * 
     * @param obj the object to invoke the method on.
     * @param methodName the name of the method to call
     * @return the object returned by the method
     */
    public static Object invoke(Object obj, String methodName) {
        try {
            Method method;
            method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Call a method on the object with one argument.
     * 
     * @param obj the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType the type of argument.
     * @param arg the value of the argument.
     * @return the object returned by the method
     */
    public static Object invoke(Object obj, String methodName, Class argType, Object arg) {
        try {
            Method method;
            method = obj.getClass().getMethod(methodName, new Class[]{argType});
            return method.invoke(obj, new Object[]{arg});
        }
        catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Call a method on the object with two argument.
     * 
     * @param obj the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType1 the type of the first argument.
     * @param arg1 the value of the first argument.
     * @param argType2 the type of the second argument.
     * @param arg2 the value of the second argument.
     * @return the object returned by the method
     */
    public static Object invoke(Object obj, String methodName, Class argType1, Object arg1, Class argType2, Object arg2) {
        try {
            Method method;
            method = obj.getClass().getMethod(methodName, new Class[]{argType1, argType2});
            return method.invoke(obj, new Object[]{arg1, arg2});
        }
        catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Get a field in an object.
     * 
     * @param obj the object to look at.
     * @param fieldName the name of the field in the object.
     * @return the value of the field.
     * @throws BuildException if there is an error.
     */
    public static Method getMethod(Object obj, String fieldName) {
        try {
            Method method = obj.getClass().getMethod("get" + fieldName);
            return method;
        }
        catch (Exception t) {
            return null; // NotReached
        }
    }

    /**
     * A method to convert an invocationTargetException to a buildexception and throw it.
     * 
     * @param t the invocation target exception.
     * @throws BuildException the converted exception.
     */
    public static void throwBuildException(Exception t) {
        throw new RuntimeException(t);
    }

    /**
     * 复制源对象中指定注解的属性值给对应的目标对象属性。如果目标对象中没有对应的setter方法，则会抛出异常。
     * 
     * @param orig 源
     * @param dest 目标
     * @param annoationClass 注解
     */
    public static void copyPropertisFilterByAnnoation(Object orig, Object dest,
            Class<? extends Annotation> annoationClass) {
        for (Field field : orig.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annoationClass)) {
                ReflectUtils.invoke(dest, "set" + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1), field.getType(),
                        ReflectUtils.getFieldValue(orig, field.getName()));
            }
        }
    }

    /**
     * 获取指定对象中指定的属性对应的值。如果出现错误，则抛出异常。
     * 
     * @param obj 指定的对象
     * @param fieldName 字段名称
     * @return 返回字段对应的值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * 克隆指定的对象及其所有属性值。
     * 
     * @param orig 指定的对象
     * @param clazz 指定克隆出来的类型
     * @return 返回克隆出来的类
     */
    public static Object cloneObjectByClass(Object orig, Class clazz) {
        Object destObj = null;
        try {
            destObj = clazz.newInstance();
            copyPropertis(orig, destObj);
        }
        catch (InstantiationException e) {
            throwBuildException(e);
        }
        catch (IllegalAccessException e) {
            throwBuildException(e);
        }
        return destObj;
    }

    /**
     * 复制两个对象间的属性值。复制两个对象中属性名称一致的属性值。
     * 
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copyPropertis(Object orig, Object dest) {
        Class clazz = dest.getClass();
        try {
            copyPropertis(orig, dest, clazz);

        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制两个对象间的属性值。复制两个对象中属性名称一致的属性值。
     * 
     * @param orig 源对象
     * @param dest 目标对象
     * @param clazz 指定类型
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyPropertis(Object orig, Object dest, Class clazz) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("set")) {
                Method getter = getMethod(orig, method.getName().replaceFirst("set", ""));
                if (getter != null) {
                    ReflectUtils.invoke(dest, method.getName(), method.getParameterTypes()[0], getter.invoke(orig));
                }
            }

        }
    }

    /**
     * 获取指定的对象对应的属性的类型
     * 
     * @param target 对象
     * @param fieldName 属性名称
     * @return 返回属性对应的类型
     */
    public static Class getFieldClass(Object target, String fieldName) {
        try {
            String[] fields = fieldName.split("\\.");
            Object obj = target;
            for (int i = 0; (fields != null) && (i < fields.length - 1); i++) {
                String method = "get".concat(fields[i].substring(0, 1).toUpperCase().concat(fields[i].substring(1)));
                obj = obj.getClass().getMethod(method).invoke(obj);
            }
            if ((fields != null) && (fields.length > 0)) {
                fieldName = fields[fields.length - 1];
            }
            fieldName = "get".concat(fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1)));
            return obj.getClass().getMethod(fieldName).getReturnType();
        }
        catch (Exception e) {
            return null;
        }
    }
}
