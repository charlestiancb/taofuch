/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.remote.xmlrpc.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * xmlrpc服务端类的定义
 * 
 * @author taofucheng
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface XmlRpcService {
    /**
     * 指定所定义的服务的调用时的名称！
     */
    String value() default "";

    /**
     * 是否使用注解指定的方法。
     * 
     * @return false:不使用<br>
     *         true:使用
     */
    boolean useMethodAnnotation() default false;
}
