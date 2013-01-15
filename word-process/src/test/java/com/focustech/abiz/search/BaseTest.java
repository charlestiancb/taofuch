/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.abiz.search;

import org.apache.commons.lang.StringUtils;

/**
 * BaseTest.java
 * 
 * @author taofucheng
 */
public abstract class BaseTest {

    protected boolean contains(String word, String[] target) {
        if (StringUtils.isNotEmpty(word) || target == null || target.length > 0) {
            for (String t : target) {
                if (word.equals(t)) {
                    return true;
                }
            }
        }
        return false;
    }
}
