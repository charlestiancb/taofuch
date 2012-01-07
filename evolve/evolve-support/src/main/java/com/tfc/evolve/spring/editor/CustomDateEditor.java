/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.editor;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import com.tfc.evolve.common.utils.DateUtils;
import com.tfc.evolve.common.utils.StringUtils;

/**
 * CustomDateEditor.java
 * 
 * @author taofucheng
 */
public class CustomDateEditor extends PropertyEditorSupport {
    private final boolean allowEmpty;
    private final String dateFormat;

    public CustomDateEditor(boolean allowEmpty, String dateFormat) {
        this.allowEmpty = allowEmpty;
        this.dateFormat = dateFormat;
    }

    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    /*
     * (non-Javadoc)
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (allowEmpty) {
            if (StringUtils.isEmpty(text)) {
                setValue(null);
                return;
            }
        }
        else if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("datetime text must not be null!");
        }
        Date date = DateUtils.parseDateWithAllDeclearedPatterns(text);
        setValue(date);
    }

    /*
     * (non-Javadoc)
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return DateUtils.parseDate2String(value, dateFormat);
    }
}
