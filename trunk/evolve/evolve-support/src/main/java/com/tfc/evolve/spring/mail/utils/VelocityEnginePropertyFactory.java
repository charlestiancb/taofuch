/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Velocity根据系统中的环境改变而改变的一个额外的属性集合。
 * 
 * @author taofucheng
 */
public class VelocityEnginePropertyFactory {
    private static ThreadLocal<List<VelocityEngineProperty>> properties =
            new ThreadLocal<List<VelocityEngineProperty>>();

    public static void putProperties(List<VelocityEngineProperty> pros) {
        if (pros != null && !pros.isEmpty()) {
            properties.set(pros);
        }
    }

    public static void pushProperties(VelocityEngineProperty pro) {
        List<VelocityEngineProperty> pros = getProperties();
        if (pros == null) {
            pros = new ArrayList<VelocityEngineProperty>();
        }
        pros.add(pro);
        putProperties(pros);
    }

    public static List<VelocityEngineProperty> getProperties() {
        return properties.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void pushPropertiesToMap(Map model) {
        List<VelocityEngineProperty> pros = getProperties();
        if (pros != null && !pros.isEmpty()) {
            for (VelocityEngineProperty pro : pros) {
                model.put(pro.getName(), pro.getValue());
            }
        }
        properties.set(null);
    }
}
