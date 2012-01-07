/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.editor;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.tfc.evolve.common.exception.SystemErrorException;

/**
 * CustomNumberEditor.java
 * 
 * @author taofucheng
 */

@SuppressWarnings("rawtypes")
public class CustomNumberEditor extends PropertyEditorSupport {

    private final Class numberClass;
    private final NumberFormat numberFormat;
    private final boolean allowEmpty;

    /**
     * Create a new CustomNumberEditor instance, using the default <code>valueOf</code> methods for parsing and
     * <code>toString</code> methods for rendering.
     * <p>
     * The "allowEmpty" parameter states if an empty String should be allowed for parsing, i.e. get interpreted as
     * <code>null</code> value. Else, an IllegalArgumentException gets thrown in that case.
     * 
     * @param numberClass Number subclass to generate
     * @param allowEmpty if empty strings should be allowed
     * @throws IllegalArgumentException if an invalid numberClass has been specified
     * @see org.springframework.util.NumberUtils#parseNumber(String, Class)
     * @see Integer#valueOf
     * @see Integer#toString
     */
    public CustomNumberEditor(Class numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }

    /**
     * Create a new CustomNumberEditor instance, using the given NumberFormat for parsing and rendering.
     * <p>
     * The allowEmpty parameter states if an empty String should be allowed for parsing, i.e. get interpreted as
     * <code>null</code> value. Else, an IllegalArgumentException gets thrown in that case.
     * 
     * @param numberClass Number subclass to generate
     * @param numberFormat NumberFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     * @throws IllegalArgumentException if an invalid numberClass has been specified
     * @see org.springframework.util.NumberUtils#parseNumber(String, Class, java.text.NumberFormat)
     * @see java.text.NumberFormat#parse
     * @see java.text.NumberFormat#format
     */
    public CustomNumberEditor(Class numberClass, NumberFormat numberFormat, boolean allowEmpty)
            throws IllegalArgumentException {
        if ((numberClass == null) || (!Number.class.isAssignableFrom(numberClass) && !numberClass.isPrimitive())) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Number num = null;
        try {
            if (allowEmpty && !StringUtils.hasText(text)) {
                num = null;
            }
            else if (numberClass.isPrimitive()) {
                if (numberClass.isAssignableFrom(long.class)) {
                    num = Long.parseLong(text);
                }
                else if (numberClass.isAssignableFrom(int.class)) {
                    num = Integer.parseInt(text);
                }
                else if (numberClass.isAssignableFrom(short.class)) {
                    num = Short.parseShort(text);
                }
                else if (numberClass.isAssignableFrom(byte.class)) {
                    num = Byte.parseByte(text);
                }
                else if (numberClass.isAssignableFrom(double.class)) {
                    num = Double.parseDouble(text);
                }
                else if (numberClass.isAssignableFrom(float.class)) {
                    num = Float.parseFloat(text);
                }
            }
            else if (numberFormat != null) {
                num = NumberUtils.parseNumber(text, numberClass, numberFormat);
            }
            else {
                num = NumberUtils.parseNumber(text, numberClass);
            }
        }
        catch (Exception e) {
            num = null;
            if (numberClass.isPrimitive()) {
                throw new SystemErrorException("numberClass.isPrimitive() == true");
            }
        }
        setValue(num);
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        }
        if (numberFormat != null) {
            return numberFormat.format(value);
        }
        else {
            return value.toString();
        }
    }

}
