/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.FieldError;

import com.tfc.evolve.common.utils.ReflectUtils;

/**
 * BindingErrorProcessor.java
 * 
 * @author taofucheng
 */
public class BindingErrorProcessor extends DefaultBindingErrorProcessor {
    protected static Log log = LogFactory.getLog(BindingErrorProcessor.class);

    @Override
    public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
        // Create field error with the exceptions's code, e.g. "typeMismatch".
        String field = ex.getPropertyChangeEvent().getPropertyName();
        Object value = ex.getPropertyChangeEvent().getNewValue();
        String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
        Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
        String errorMessage = null;
        try {
            errorMessage += getErrorMessage(bindingResult, field);
        }
        catch (Exception e) {
            log.error(e);
            errorMessage += "数据错误！";
        }
        bindingResult.addError(new FieldError(bindingResult.getObjectName(), field, value, true, codes, arguments,
                errorMessage));
    }

    @SuppressWarnings({"rawtypes"})
    protected String getErrorMessage(BindingResult bindingResult, String field) throws IllegalArgumentException,
            SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class fieldClass = ReflectUtils.getFieldClass(bindingResult.getTarget(), field);
        if ((fieldClass == Integer.class) || (fieldClass == Long.class)) {
            return "不是整数！";
        }
        else if (Number.class.isAssignableFrom(fieldClass)) {
            return "不是数字！";
        }
        else if (Date.class.isAssignableFrom(fieldClass)) {
            return "不是日期格式！";
        }
        else {
            return "数据错误！";
        }
    }
}
