/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.web;

import java.util.Date;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

import com.tfc.evolve.spring.editor.CustomDateEditor;
import com.tfc.evolve.spring.editor.CustomNumberEditor;
import com.tfc.evolve.spring.processor.BindingErrorProcessor;

/**
 * WebBindingInitializer.java
 * 
 * @author taofucheng
 */
public class WebBindingInitializer implements org.springframework.web.bind.support.WebBindingInitializer {
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(true, dateFormat));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
        binder.registerCustomEditor(int.class, new CustomNumberEditor(int.class, true));
        binder.registerCustomEditor(long.class, new CustomNumberEditor(long.class, true));
        binder.registerCustomEditor(double.class, new CustomNumberEditor(double.class, true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));

        binder.setBindingErrorProcessor(new BindingErrorProcessor());
    }
}
