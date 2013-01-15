/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.convert;

import java.io.Serializable;

/**
 * 词与拼音间相互的一对多关系
 * 
 * @author taofucheng
 */
public class WordMapper implements Serializable {
    private static final long serialVersionUID = 7830273642658350190L;
    private String key;
    private String[] values;

    public WordMapper() {

    }

    public WordMapper(String key) {
        setKey(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
