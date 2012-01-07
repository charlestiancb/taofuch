/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.velocity.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * 用于jpg gif等图片或其他二进制格式内容的view，只能支持一种格式，因此如果要支持两种格式，在abiz-servlet中需要配置两次
 * 
 * @author geliang
 */
public class BlobView extends AbstractView {

    public static final String DEFAULT_CONTENT_TYPE = "image/jpeg";

    public BlobView(String contentType) {
        if (contentType == null) {
            setContentType(DEFAULT_CONTENT_TYPE);
        }
        else {
            setContentType(contentType);
        }
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", "attachment");
    }
}
