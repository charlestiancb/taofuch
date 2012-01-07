/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail.utils;

import java.io.Serializable;

/**
 * Velocity属性
 * 
 * @author taofucheng
 */
public class VelocityEngineProperty implements Serializable {
    private static final long serialVersionUID = 530864730062618116L;
    private String name;
    private Object value;
    /** 对该属性的一些描述 */
    private String desc;

    public VelocityEngineProperty(String propertyName, Object propertyValue) {
        setName(propertyName);
        setValue(propertyValue);
    }

    public VelocityEngineProperty(String propertyName, Object propertyValue, String propertyDesc) {
        setName(propertyName);
        setValue(propertyValue);
        setDesc(propertyDesc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String toString() {
        return name + ": " + value;
    }
}
